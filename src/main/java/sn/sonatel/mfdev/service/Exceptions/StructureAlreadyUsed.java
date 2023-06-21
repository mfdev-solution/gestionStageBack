package sn.sonatel.mfdev.service.Exceptions;

public class StructureAlreadyUsed extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StructureAlreadyUsed(String msg) {
        super(msg);
    }
}
