package sn.sonatel.mfdev.service.Exceptions;

public class InternAlreadyUsed extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternAlreadyUsed(String msg) {
        super(msg);
    }
}
