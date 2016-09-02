package net.buchlese.verw.ctrl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.querydsl.core.types.Predicate;

import net.buchlese.bofc.api.bofc.Command;
import net.buchlese.bofc.api.bofc.PosIssueSlip;
import net.buchlese.verw.repos.CommandRepository;


@RestController
@RequestMapping(path="commands")
public class CommandController {

	@Autowired CommandRepository commandRepository;

	@ResponseBody
	@RequestMapping(path="commandsDyn", method = RequestMethod.GET)
	public Page<Command> commandsDynamic(@QuerydslPredicate(root = PosIssueSlip.class) Predicate predicate,    
	          Pageable pageable, @RequestParam MultiValueMap<String, String> parameters) {

		return commandRepository.findAll(predicate, pageable);
	}

	@ResponseBody
	@RequestMapping(path="getCmds", method = RequestMethod.GET)
	@Transactional
	public List<Command> handoutCommands(@RequestParam("pointid") Integer pointid) {
		List<Command> cmds = commandRepository.findAllByPointid(pointid);
		return cmds;
	}

	@RequestMapping(path="acceptCmdAnswer", method = RequestMethod.POST)
	@Transactional
	public ResponseEntity<?> acceptInvoice(@RequestBody Command cmd)  {
		try {
			Command old = commandRepository.findOne(cmd.getId());
			if (old != null) {
				old.setResult(cmd.getResult());
				commandRepository.save(old);
			}
			return ResponseEntity.ok().build();
		} catch (Throwable t) {
			return ResponseEntity.unprocessableEntity().body(t.getMessage());
		}
	}

}
