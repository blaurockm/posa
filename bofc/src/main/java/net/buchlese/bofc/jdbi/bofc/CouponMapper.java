package net.buchlese.bofc.jdbi.bofc;

import io.dropwizard.jackson.Jackson;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.buchlese.bofc.api.coupon.Coupon;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CouponMapper implements ResultSetMapper<Coupon>{

	@Override
	public Coupon map(int arg0, ResultSet rs, StatementContext arg2) throws SQLException {
		Coupon tx = new Coupon();
		ObjectMapper om = Jackson.newObjectMapper();
		try {
			tx = om.readValue(rs.getString("complJson"), Coupon.class);
		} catch (IOException e) {
			throw new SQLException(e);
		}
		tx.setId(rs.getLong("id"));
		tx.setPayed(rs.getBoolean("payed"));
		return tx;
	}

}
