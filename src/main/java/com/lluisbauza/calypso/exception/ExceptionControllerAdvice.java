package com.lluisbauza.calypso.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({ReservationNotFoundException.class, SlotNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundExceptions(
            RuntimeException e)
    {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorDetails", new ErrorDetails(e.getMessage()));

        return modelAndView;
    }

    @ExceptionHandler({ReservationAlreadyCancelledException.class, SlotNotAvailableException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handleConflictExceptions(
            RuntimeException e)
    {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorDetails", new ErrorDetails(e.getMessage()));

        return modelAndView;
    }

    @ExceptionHandler({CapacityExceededException.class, InvalidPaxException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadRequestExceptions(
            RuntimeException e)
    {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorDetails", new ErrorDetails(e.getMessage()));

        return modelAndView;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(
            Exception e)
    {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.addObject("errorDetails", new ErrorDetails(e.getMessage()));

        return modelAndView;
    }

}
