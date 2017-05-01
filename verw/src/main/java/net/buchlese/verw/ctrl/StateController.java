package net.buchlese.verw.ctrl;


import java.util.List;
import java.util.Optional;

import net.buchlese.bofc.api.bofc.PosState;
import net.buchlese.bofc.api.bofc.PosSyncState;
import net.buchlese.bofc.api.bofc.ServerState;
import net.buchlese.verw.repos.SyncStateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path="state")
public class StateController {

	@Autowired SyncStateRepository stateRepository;

	@RequestMapping(path="acceptPosState", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptPosState(@RequestBody PosState state)  {
		try {
			List<PosSyncState> oldList = stateRepository.findByPointid(state.getPointid());
			for (PosSyncState syst : state.getSyncStates()) {
				// gibt es den schon?
				Optional<PosSyncState> old = oldList.stream().filter(x -> x.getKey().equals(syst.getKey())).findFirst();
				if (old.isPresent()) {
					syst.setId(old.get().getId()); // damit wird gezeigt, dass wir ein update sind
					stateRepository.saveAndFlush(syst);
				} else {
					stateRepository.saveAndFlush(syst);
				}
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}

	@RequestMapping(path="acceptServerState", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptServerState(@RequestBody ServerState state)  {
		return ResponseEntity.ok().build();
	}

}
