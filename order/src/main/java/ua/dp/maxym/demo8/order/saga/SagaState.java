package ua.dp.maxym.demo8.order.saga;

public enum SagaState {
    RESERVING_SKUs,
    PAYING,
    CONFIRMING_SKU,
    CONFIRMING_ORDER,
    REJECTING_ORDER,
    SUCCEEDED,
    FAILED
}
