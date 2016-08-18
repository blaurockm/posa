package net.buchlese.verw.ctrl;


import net.buchlese.bofc.api.bofc.QPosCashBalance;

import com.querydsl.core.types.dsl.BooleanExpression;


public class BalanceExpressions {

	private BalanceExpressions() {};
	
	public static BooleanExpression wasExported() {
		return QPosCashBalance.posCashBalance.exported.isTrue();	
	}

	public static BooleanExpression wasNotExported() {
		return QPosCashBalance.posCashBalance.exported.isFalse();	
	}
}
