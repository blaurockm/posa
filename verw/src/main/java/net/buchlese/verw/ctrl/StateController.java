package net.buchlese.verw.ctrl;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.buchlese.bofc.api.bofc.PosState;
import net.buchlese.bofc.api.bofc.ServerState;


@RestController
@RequestMapping(path="technical")
public class StateController {

	@RequestMapping(path="acceptPosState", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptPosState(@RequestBody PosState state)  {
		return ResponseEntity.ok().build();
	}

	@RequestMapping(path="acceptServerState", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptServerState(@RequestBody ServerState state)  {
		return ResponseEntity.ok().build();
	}

}
