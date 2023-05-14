package utils;

import java.time.LocalDate;
import java.util.Optional;

public class Query {

    Optional <LocalDate> startDate;
    Optional <LocalDate> endDate;
    Optional <String> interval;
    Optional <Integer> timePeriod;
    Optional <OrderDirection> orderDirection;

    /**
     * Constructor to create a new query object
     * @param startDate
     * @param endDate
     * @param interval
     * @param timePeriod
     * @param orderDirection
     */
    public Query(Optional<LocalDate> startDate, Optional<LocalDate> endDate, Optional <String> interval, Optional <Integer> timePeriod, Optional <OrderDirection> orderDirection) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.interval = interval;
        this.timePeriod = timePeriod;
        this.orderDirection = orderDirection;
    }

    /**
     * Copy constructor to create new query object from an existing query object
     * @param copy - Original query
     */
    public Query(Query copy) {
        startDate = copy.getStartDate();
        endDate = copy.getEndDate();
        interval = copy.getInterval();
        timePeriod = copy.getTimePeriod();
        orderDirection = copy.getOrderDirection();
    }

    /**
     *
     * @return the start date from the query
     */
    public Optional<LocalDate> getStartDate() {
        return startDate;
    }

    /**
     * set the start date for a search query
     * @param startDate
     */
    public void setStartDate(Optional<LocalDate> startDate) {
        this.startDate = startDate;
    }

    /**
     *
     * @return the end date from the query
     */
    public Optional<LocalDate> getEndDate() {
        return endDate;
    }

    /**
     * set the end date for a search query
     * @param endDate
     */
    public void setEndDate(Optional<LocalDate> endDate) {
        this.endDate = endDate;
    }

    /**
     *
     * @return the interval from the query
     */
    public Optional <String> getInterval() {
        return interval;
    }

    /**
     * set the time interval for a search query
     * @param interval
     */
    public void setInterval(Optional <String> interval) {
        this.interval = interval;
    }

    /**
     *
     * @return the time period from the query
     */
    public Optional <Integer> getTimePeriod() {
        return timePeriod;
    }

    /**
     * set the time period for a search query
     * @param timePeriod
     */
    public void setTimePeriod(Optional <Integer> timePeriod) {
        this.timePeriod = timePeriod;
    }

    /**
     *
     * @return the order direction from the query
     */
    public Optional <OrderDirection> getOrderDirection() {
        return orderDirection;
    }

    /**
     * set the order direction for a search query
     * @param orderDirection
     */
    public void setOrderDirection(Optional <OrderDirection> orderDirection) {
        this.orderDirection = orderDirection;
    }

}
