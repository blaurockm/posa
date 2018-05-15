package net.buchlese.bofc.jpa;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import io.dropwizard.hibernate.AbstractDAO;
import net.buchlese.bofc.api.bofc.SequenceGen;

public class JpaSequenceGenDAO extends AbstractDAO<SequenceGen>   {

	public JpaSequenceGenDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

    public SequenceGen findById(Long id) {
        return get(id);
    }

    public void create(SequenceGen person) {
        currentSession().save(person);
    }

    public void update(SequenceGen person) {
        currentSession().saveOrUpdate(person);
    }

    public void delete(SequenceGen person) {
        currentSession().delete(person);
    }

	public SequenceGen findByKey(String key) {
		return (SequenceGen) criteria().add(Restrictions.eq("seqKey", key)).uniqueResult();
	}

}
