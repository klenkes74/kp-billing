/**
 * The notitia subsystem is a revision save history of customer, tarif and product data.
 *
 * <p>It keeps a trail of changes that can be combined in a complete history of what changed when. There is an element
 * to save also who did the change but that relies on the integration in the comunication infrastructure and is
 * therefore more optional.</p>
 *
 * <p>For all change events the event is saved into the events hierarchie while a current snapshot is held for faster
 * access. It follows the architectural pattern of command-and-query-separation.</p>
 *
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-21
 */
