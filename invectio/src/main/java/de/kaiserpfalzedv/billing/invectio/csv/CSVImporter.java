/*
 *    Copyright 2018 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.kaiserpfalzedv.billing.invectio.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.imported.ImporterService;
import de.kaiserpfalzedv.billing.api.imported.ImportingException;
import de.kaiserpfalzedv.billing.api.imported.IncompatibleImportDataException;
import de.kaiserpfalzedv.billing.api.imported.IncompleteImportDataException;
import de.kaiserpfalzedv.billing.api.imported.RawBaseRecord;
import de.kaiserpfalzedv.billing.invectio.RawRecordBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-15
 */
public class CSVImporter implements ImporterService {
    private static final Logger LOG = LoggerFactory.getLogger(CSVImporter.class);

    private static final ZoneId UTC = ZoneId.of("UTC");


    @Override
    public List<? extends RawBaseRecord> execute(Reader reader)
            throws ImportingException {
        List<? extends RawBaseRecord> result;

        BufferedReader br = new BufferedReader(reader);

        try (CSVParser csvParser = new CSVParser(br, CSVFormat.DEFAULT)) {
            CSVHeader header = readHeader(csvParser);
            result = readData(csvParser, header);

            checkCompleteDataSet(header, result);
        } catch (IOException e) {
            throw new ImportingException("Can't open data reader.", e);
        }


        return result;
    }

    private CSVHeader readHeader(CSVParser parser) throws ImportingException {
        CSVHeader result = new CSVHeader();

        try {
            parseCSVFile(parser, result);

            readBaseDataFromHeader(result);
            readTagTitlesFromHeader(result);
        } catch (IOException e) {
            throw new ImportingException("IO Error while reading the CSV header.", e);
        }

        return result;
    }

    private void parseCSVFile(CSVParser parser, CSVHeader result) throws IOException {
        Iterable<CSVRecord> csvRecords = parser.getRecords();

        result.records = csvRecords.iterator();
    }

    private void readBaseDataFromHeader(CSVHeader result) throws ImportingException {
        if (result.records.hasNext()) {
            CSVRecord transactionLine = result.records.next();
            result.timestamp = readTimestampFromHeader(transactionLine);
            result.numberOfRecordsInTransaction = readNumberOfRecordsFromHeader(transactionLine);
            result.transactionId = readTransactionFromHeader(transactionLine);
        } else {
            throw new ImportingException("Empty data set.");
        }
    }

    private OffsetDateTime readTimestampFromHeader(final CSVRecord transactionLine) throws ImportingException {
        try {
            return OffsetDateTime.parse(transactionLine.get(0));
        } catch (DateTimeParseException e) {
            throw new ImportingException("No valid timestamp for the data set given!", e);
        }
    }

    private int readNumberOfRecordsFromHeader(final CSVRecord transactionLine) throws ImportingException {
        try {
            return Integer.parseInt(transactionLine.get(1));
        } catch (NumberFormatException e) {
            throw new ImportingException("No valid number of records in data set given!", e);
        }
    }

    private UUID readTransactionFromHeader(final CSVRecord transactionLine) throws ImportingException {
        try {
            return UUID.fromString(transactionLine.get(2));
        } catch (IllegalArgumentException e) {
            throw new ImportingException("Transaction id is no valid UUID!", e);
        }
    }

    private void readTagTitlesFromHeader(CSVHeader result) {
        if (result.records.hasNext()) {
            CSVRecord headerLine = result.records.next();

            result.metadata = readColumnsFromHeader(headerLine);

            readTagTitles(result);
        }
    }

    private void readTagTitles(CSVHeader result) {
        result.tags = new String[result.metadata.getColumnCount() - 3];
        LOG.debug("CSV data contains {} tag columns.", result.metadata.getColumnCount() - 3);

        for(int i = 3; i < result.metadata.getColumnCount(); i++) {
            result.tags[i-3] = result.metadata.getColumnName(i);
            
            LOG.trace("New tag title: {}", result.tags[i-3]);
        }
    }


    private CSVResultSetMetaData readColumnsFromHeader(final CSVRecord record) {
        return new CSVResultSetMetaData(record);
    }

    
    
    private List<RawBaseRecord> readData(CSVParser parser, final CSVHeader header) throws ImportingException  {
        final ArrayList<RawBaseRecord> result = new ArrayList<>();

        while (header.records.hasNext()) {
            readRecord(result, header.records.next(), header);
        }

        return result;
    }
    
    private void readRecord(List<RawBaseRecord> result, final CSVRecord record, final CSVHeader header)
            throws IncompatibleImportDataException {
        RawRecordBuilder<RawBaseRecord> data = new RawRecordBuilder<>();

        readBaseData(header, data);
        readMeteringData(record, header, data);
        readTags(record, header, data);

        result.add(data.build());
    }

    private void readBaseData(final CSVHeader header, RawRecordBuilder<RawBaseRecord> data) {
        data
                .setMeteringId(header.transactionId.toString())
                .setImportedDate(header.importedDate)
                .setMeteringProduct("")
                .setMeteredCustomer("");
    }

    private void readMeteringData(final CSVRecord record, final CSVHeader header, RawRecordBuilder<RawBaseRecord> data)
            throws IncompatibleImportDataException {
        String meteredValue = record.get(0);

        try {
            if (isNotBlank(meteredValue)) {
                data.setMeteredValue(BigDecimal.valueOf(Double.parseDouble(record.get(0))));
            }
            
            OffsetDateTime meteredStart = OffsetDateTime.parse(record.get(1));
            OffsetDateTime meteredEnd   = OffsetDateTime.parse(record.get(2));
            data.setMeteredStartDate(meteredStart);
            data.setMeteredDuration(Duration.between(meteredStart, meteredEnd));
            data.setValueDate(meteredStart);
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IncompatibleImportDataException(header.transactionId, (int) record.getRecordNumber());
        }
    }

    private void readTags(final CSVRecord record, final CSVHeader header, RawRecordBuilder<RawBaseRecord> data) {
        String[] tags = new String[header.tags.length];
        for (int i = 0; i < header.tags.length ; i++) {
            tags[i] = record.get(i + 3);
        }
        data.setTagTitles(header.tags);
        data.setTags(tags);
    }


    private void checkCompleteDataSet(CSVHeader header, List<? extends RawBaseRecord> data) throws ImportingException {
        if (header.numberOfRecordsInTransaction != data.size()) {
            throw new  IncompleteImportDataException(header.transactionId, header.numberOfRecordsInTransaction,
                                                     data.size());
        }
    }

    private class CSVHeader {
        OffsetDateTime timestamp;
        int numberOfRecordsInTransaction;
        UUID transactionId;
        CSVResultSetMetaData metadata;
        String[] tags;
        OffsetDateTime importedDate = OffsetDateTime.now(UTC);

        Iterator<CSVRecord> records;
    }
    
    private class CSVResultSetMetaData implements ResultSetMetaData {

        private CSVRecord record;

        private CSVResultSetMetaData(final CSVRecord record) {
            this.record = record;
        }

        @Override
        public int getColumnCount() {
            return record.size();
        }

        @Override
        public boolean isAutoIncrement(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public boolean isCaseSensitive(int column) throws SQLException {
            return false;
        }

        @Override
        public boolean isSearchable(int column) throws SQLException {
            return false;
        }

        @Override
        public boolean isCurrency(int column) throws SQLException {
            return false;
        }

        @Override
        public int isNullable(int column) throws SQLException {
            return 0;
        }

        @Override
        public boolean isSigned(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public int getColumnDisplaySize(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public String getColumnLabel(int column) {
            return getColumnName(column);
        }

        @Override
        public String getColumnName(int column) {
            return record.get(column);
        }

        @Override
        public String getSchemaName(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public int getPrecision(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public int getScale(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public String getTableName(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public String getCatalogName(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public int getColumnType(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public String getColumnTypeName(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public boolean isReadOnly(int column) throws SQLException {
            return true;
        }

        @Override
        public boolean isWritable(int column) throws SQLException {
            return false;
        }

        @Override
        public boolean isDefinitelyWritable(int column) throws SQLException {
            return false;
        }

        @Override
        public String getColumnClassName(int column) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            throw new UnsupportedOperationException("Sorry Dave, I can't do that!");
        }
    }
}
