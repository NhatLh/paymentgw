package com.digitechlabs.paymentgw.utils;

public class GlobalVariables {

    public static final String CURRENCY_USD = "USD";
    public static final String CURRENCY_EUR = "EUR";
    public static final String CURRENCY_AVA = "AVA";
    public static final String CURRENCY_GBP = "GBP";
    public static final String CURRENCY_AUD = "AUD";
    public static final String CURRENCY_SGD = "SGD";
    public static final String CURRENCY_CAD = "CAD";
    public static final String CURRENCY_NZD = "NZD";

    public static final String PAY_WALLET = "WALLET";
    public static final String PAY_COINGATE = "COINGATE";
    public static final String PAY_PAYPAL = "PAYPAL";
//    public static final String PAY_PAYPAL = "PAYPAL";

    public static final String TRANSACTION_TYPE_PAYPAL = "PAYPAL";
    public static final String TRANSACTION_TYPE_LOCK = "Locked";
    public static final String TRANSACTION_TYPE_UNLOCK = "Unlocked";

    public static final String[] ORDER_ARRAY = {"order_id", "price_amount", "price_currency", "receive_currency", "title", "description", "callback_url", "cancel_url", "success_url", "token"};
    public static final String[] CHECKOUT_ARRAY = {"pay_currency"};
    public static final String[] GETORDER_ARRAY = {"pay_currency"};
    public static final String[] GET_PAYPAL_TOKEN = {"grant_type"};
    public static final String SQL_INSERT_ORDER_HIS = "insert into transaction_history (order_id, price_amount, price_currency, title, description, created_at, token, receive_currency, receive_amount, pay_amount, pay_currency, status, id, updated_at, visibility, user_id, revenue, pay_type)"
            + " values (?,?,?,?,?,?,?,?,?,?,?,?,?, now(),?,?,?::JSON,?)";

    public static final String SQL_INSERT_PAID_NOTIFY_HIS = "INSERT INTO paid_notify_history(order_id, created_at, type, status) VALUES (?,now(),?,?)";

    public static final String SQL_SELECT_CHECK_COINGATE_CALLBACK = "SELECT * FROM transaction_history WHERE order_id = ? AND status = ? AND pay_type = ?";
    public static final String SQL_SELECT_CHECK_PAID_NOTIFY = "select * from paid_notify_history where order_id = ?";

    public static final String COINGATE_STATUS_NEW = "new";
    public static final String COINGATE_STATUS_PENDING = "pending";
    public static final String COINGATE_STATUS_CONFIRMING = "confirming";
    public static final String COINGATE_STATUS_PAID = "paid";
    public static final String COINGATE_STATUS_INVALID = "invalid";
    public static final String COINGATE_STATUS_EXPIRED = "expired";
    public static final String COINGATE_STATUS_CANCELED = "canceled";
    public static final String COINGATE_STATUS_REFUNDED = "refunded";
    public static final String[] NOTIFY_SERVICE_ARRAY = {"order_id"};
    public static final String[] WITHDRAW_ARRAY = {"currency", "amount", "to_address"};

    public static final String TRANSACTION_STATUS_START = "Need to confirm";
    public static final String TRANSACTION_STATUS_PENDING = "Pending";
    public static final String TRANSACTION_STATUS_EXPIRED = "Expired";
    public static final String TRANSACTION_STATUS_SUCCESS = "Success";
    public static final String TRANSACTION_STATUS_FAIL = "Failed";
    public static final String TRANSACTION_STATUS_CANCEL = "Canceled";
    public static final String TRANSACTION_STATUS_CONFIRMING = "Confirming";

    public static final String PAYPAL_STATUS_PAYMENT_PENDING = "PAYMENT.SALE.PENDING";
    public static final String PAYPAL_STATUS_PAYMENT_COMPLETE = "PAYMENT.SALE.COMPLETED";
    public static final String PAYPAL_STATUS_REFUND_COMPLETE = "PAYMENT.SALE.REFUNDED";

//    public static final String PAYPAL_REFUND_STATUS_SUCCESS = "completed";
    public static final String SQL_SELECT_USERID_FOR_UPDATE_BALANCE = "SELECT id, balance, award, wallet_add FROM user_wallet WHERE user_id =? AND wallet_type =? AND is_active = true FOR UPDATE";
    public static final String SQL_SELECT_USERID_FOR_UPDATE_BLOCK = "SELECT id, balance, award,blocked, wallet_add FROM user_wallet WHERE user_id =? AND wallet_type =? AND is_active = true FOR UPDATE";
    public static final String SQL_SELECT_ADDRESS_FROM_USERID_CURRENCY = "SELECT wallet_add FROM user_wallet WHERE user_id =? AND wallet_type =? AND is_active = true FOR UPDATE";
    public static final String SQL_SELECT_TRANS_ID = "SELECT count(*) FROM withdraw_his WHERE transaction_id =?";
    public static final String SQL_SELECT_ADDRESS = "SELECT user_id FROM user_wallet WHERE wallet_add = ? AND wallet_type =? AND is_active = true ";
    public static final String SQL_UPDATE_BALANCE = "UPDATE user_wallet SET balance = balance +? WHERE wallet_add = ? AND wallet_type =? AND is_active = true ";
    public static final String SQL_UPDATE_BALANCE_BY_USER = "UPDATE user_wallet SET balance = balance +? WHERE user_id = ? AND wallet_type =? AND is_active = true ";

    public static final String SQL_INSERT_WITHDRAW_HIS = "insert into withdraw_his (user_id, currency, amount, to_address, max_per_day,event, created_at, status, transaction_id, request_id) VALUES (?,?, ?, ?, ?,?, now(),?,?,?)";
    public static final String SQL_INSERT_DEPOSIT_HIS = "insert into deposit_his (type, event, currency, txid,timestamp,address, amount, created_at, user_id, from_address) values (?,?,?,?,?,?,?, now(),?, ?)";
    public static final String SQL_CHECK_WITHDRAW = "select coalesce(sum(amount),0) from withdraw_his a where a.user_id = ? and a.currency = ? and a.event in ('completed','Success') and a.created_at >= date_trunc('day', now())";
    public static final String SQL_GET_BALANCE = "select balance, blocked, award from user_wallet where user_id = ? and wallet_type = ? and is_active = true";
    public static final String SQL_GET_HIS_TRANSID = "select transaction_id from history where id = ? and transaction_type = ?";
    public static final String SQL_GET_ID_FROM_HIS = "select id from history where order_id = ? and transaction_type = ?";
    public static final String SQL_GET_STATUS_FROM_HIS = "SELECT status FROM history WHERE id = ? AND transaction_type = ?";
    public static final String SQL_CHECK_ADDRESS_EXIST = "select user_id from user_wallet where wallet_add = ? and wallet_type = ?";
    public static final String SQL_CHECK_TX_DEPOSIT = "select count(*) from deposit_his where txid = ? and address = ?";
    public static final String SQL_CHECK_TX_WITHDRAW = "select count(*) from withdraw_his where tx = ?";

    public static final String SQL_SELECT_HIS_PAY = "SELECT id, status, amount, created_at, currency FROM history WHERE user_id =? and created_at > ? and created_at < ? AND is_deleted = 'false' AND transaction_type = 'PAY' order by created_at desc";
    public static final String SQL_SELECT_HIS_BY_TYPE = "SELECT id, status, amount, created_at,from_address, to_address, currency,transaction_type, order_id, transaction_id, note FROM history WHERE user_id =? and created_at > ? and created_at < ? AND is_deleted = 'false' AND transaction_type_id in (?) order by created_at desc";
    public static final String SQL_SELECT_HIS_WITHDRAW = "SELECT id, status, amount, created_at, to_address, currency FROM history WHERE user_id =? and created_at > ? and created_at < ? AND is_deleted = 'false' AND transaction_type = 'WITHDRAW' order by created_at desc";
    public static final String SQL_SELECT_HIS = "SELECT id, status, amount, created_at,from_address, to_address, currency,transaction_type, order_id, transaction_id, note, transaction_type_id FROM history WHERE user_id =? and created_at > ? and created_at < ? AND is_deleted = 'false' AND transaction_type_id in (?,?,?,?,?,?) order by created_at desc";

    public static final String SQL_UPDATE_USER_STATUS_TRANSACTION = "UPDATE transaction_history set is_deleted = 'true' WHERE user_id = ?";
    public static final String SQL_UPDATE_USER_STATUS_DEPOSIT = "UPDATE deposit_his set is_deleted = 'true' WHERE user_id = ?";
    public static final String SQL_UPDATE_USER_STATUS_WITHDRAW = "UPDATE withdraw_his set is_deleted = 'true' WHERE user_id = ?";
    public static final String SQL_UPDATE_USER_STATUS_HISTORY = "UPDATE history set is_deleted = 'true' WHERE user_id = ?";

    public static final String SELECT_INFO = "SELECT user_id, amount FROM withdraw_his WHERE transaction_id =?";
    public static final long MILIS_DAY = 86400000;
    public static final long THRESHOLD_HIS = 7776000000L;

    public static final String SQL_SELECT_EMAIL = "SELECT email FROM auth_user WHERE id =?";
    public static final String SQL_SELECT_USER_WALLET = "SELECT wallet_add, wallet_type, is_active, balance, award FROM user_wallet WHERE user_id =? and is_active = true LIMIT 1";

    public static final String INSERT_HISTORY = "INSERT INTO history (id, status, amount, created_at, from_address, to_address, transaction_type, user_id, expired_time, currency, order_id,transaction_id, transaction_type_id, note) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_STATUS_HISTORY = "UPDATE history SET status = ? WHERE id = ? and transaction_type = ?";

    public static final String SELECT_GET_REQUEST_ID_FROM_TRANSACTION_ID = "SELECT request_id FROM withdraw_his WHERE transaction_id = ? AND event = 'Pending'";

    public static final String[] ARRAY_HIS = {"WITHDRAW", "DEPOSIT", "PAY"};
    public static final String[] ARRAY_HIS_DEPOSIT = {"DEPOSIT"};
    public static final String[] ARRAY_HIS_WITHDRAW = {"WITHDRAW"};
    public static final String[] ARRAY_HIS_BOOKING = {"PAY"};

    public static final String MSG_NOT_ENOUGH_BALANCE = "You do not have enough funds in your Travala wallet to make the withdrawal request.";
    public static final String MSG_LIMITED_WITHDRAW = "You can not make the withdrawal. Your total withdrawal today is #WTDRAWED# AVA. Maximum per day is #MAXIMUM# AVA.";

    public static final String SQL_SYNC_RATE = "SELECT key, rate FROM currency";
    public static final String SQL_SELECT_REVENUE = "SELECT revenue -> 'USD' as USD, revenue -> 'EUR' as EUR, revenue -> 'AVA' as AVA, pay_type FROM transaction_history WHERE status in ('SUCCESS','paid') AND created_at >= ? AND created_at < ?";
    public static final String SQL_SELECT_ORDER_DETAIL_TYPE1 = "SELECT order_id, pay_currency, pay_amount, status FROM transaction_history WHERE order_id in (#LIST#) AND status in ('SUCCESS','paid','canceled','expired','success')";
//    public static final String SQL_SELECT_ORDER_DETAIL_TYPE1 = "SELECT order_id, currency, amount, status FROM history WHERE order_id in (#LIST#)";

}
