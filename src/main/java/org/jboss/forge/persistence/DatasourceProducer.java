package org.jboss.forge.persistence;

import org.jboss.seam.solder.core.ExtensionManaged;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.io.Serializable;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@ConversationScoped
public class DatasourceProducer implements Serializable {
    private static final long serialVersionUID = -5267593171036179836L;

    @ExtensionManaged
    @Produces
    @PersistenceUnit
    @ConversationScoped
    EntityManagerFactory producerField;
}
