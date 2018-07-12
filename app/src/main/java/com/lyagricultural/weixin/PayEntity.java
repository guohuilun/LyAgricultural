package com.lyagricultural.weixin;

/**
 * 支付实体类
 * Created by cheng on 2016/7/30.
 */
public class PayEntity {
    private PayType payType; //支付类型
    private Status status;//支付状态

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public enum Status {
        PAY_SUCCESS(1),//支付成功
         PAY_WAIT_CONFIRM(2),//支付结果确认中
        PAY_FAIL(3) ,//支付失败
        PAY_CANCEL(4);//取消支付


        private int value;

        private Status(int value) {
            this.value = value;

        }

        public int getValue() {
            return value;
        }
    }
    public enum PayType {
        ALI_PAY(1),//支付宝
        Wx_PAY(2),//微信支付
        UNION_PAY(3) ;//银联支付

        private int value;

        private PayType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
