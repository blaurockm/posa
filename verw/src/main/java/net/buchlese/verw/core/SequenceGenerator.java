package net.buchlese.verw.core;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.buchlese.bofc.api.sys.SequenceGen;

@Service
@Scope(value="singleton")
public class SequenceGenerator {

	@PersistenceContext	EntityManager em;
	
	
	@Transactional
	public void initSequence(String key, long start, LocalDate validFrom) {
		SequenceGen gen = getCurrentActiveSequence(key);
		if (gen != null) {
			throw new IllegalArgumentException("key already exists");
		}
		gen = new SequenceGen();
		gen.setSeqKey(key);
		gen.setStart(start);
		gen.setCurrent(start);
		gen.setValidFrom(validFrom);
		em.persist(gen);
	}

	@Transactional
	public Long getNextNumber(String key) {
		SequenceGen gen = getCurrentActiveSequence(key);
		if ( gen != null) {
			Long next = gen.getCurrent() + 1;
			gen.setCurrent(next);
			em.persist(gen);
			return next;
		}
		return null;
	}
	
	@Transactional
	public Iterable<SequenceGen> getStatistics() {
		List<SequenceGen> res = em.createQuery("select * from SequenceGen",SequenceGen.class).getResultList();
		return res;
	}
	
	
	private SequenceGen getCurrentActiveSequence(String key) {
		List<SequenceGen> res = em.createQuery("from SequenceGen where seqKey = ?",SequenceGen.class).setParameter(1, key).getResultList();
		if (res.isEmpty() == false) {
			return res.get(0);
		}
		return null; 
	}
	
	
}
