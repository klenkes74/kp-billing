# kp-billing
Small Billing-Engine

## Sub Modules

### invectio
The import engine. This module imports the raw records via a public interface. There are different adapters needed for
the datasources to transfer the data source native format to the import format.

### princeps
The guiding engine. This engine decides on the customer and the product of the record.

### ratio
The rating engine. This module rates the record on basis of the customer and product of the record.

### quod
The generator for the itemised bill. I come from telco, so I call it CDR (Call Data Record) most of the times.

### libellum
The invoice and report engine. This module generates the invoices and reports for cucstomers based on the rated
records.

### commune
Common classes like the record sets to work on (data interchange format)
