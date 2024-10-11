package com.sparta.payment.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.payment.domain.entity.Payment;
import com.sparta.payment.domain.entity.PaymentState;
import com.sparta.payment.domain.entity.QPayment;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryCustomImpl extends QuerydslRepositorySupport implements
    PaymentRepositoryCustom {

  @Autowired
  private JPAQueryFactory queryFactory;

  public PaymentRepositoryCustomImpl() {
    super(Payment.class);
  }


  @Override
  public Page<Payment> findBySearchOption(Pageable pageable, String userId, String paymentKey,
      String paymentId, String orderId, String state) {
    JPAQuery<Payment> query = queryFactory.selectFrom(QPayment.payment)
        .where(eqUserId(userId), eqPaymentKey(paymentKey), eqPaymentId(paymentId),
            eqOrderId(orderId),
            eqState(state)
        );
    List<Payment> payments = this.getQuerydsl().applyPagination(pageable, query).fetch();
    return new PageImpl<Payment>(payments, pageable, query.fetchCount());
  }

  private BooleanExpression eqPaymentKey(String paymentKey) {
    return paymentKey != null ? QPayment.payment.paymentKey.eq(paymentKey) : null;
  }

  private BooleanExpression eqPaymentId(String paymentId) {
    return paymentId != null ? QPayment.payment.paymentId.eq(Long.parseLong(paymentId)) : null;
  }

  private BooleanExpression eqOrderId(String orderId) {
    return orderId != null ? QPayment.payment.orderId.eq(Long.parseLong(orderId)) : null;
  }

  private BooleanExpression eqUserId(String userId) {
    return userId != null ? QPayment.payment.userId.eq(Long.parseLong(userId)) : null;
  }

  private BooleanExpression eqState(String state) {
    return state != null ? QPayment.payment.state.eq(PaymentState.valueOf(state)) : null;
  }


}
