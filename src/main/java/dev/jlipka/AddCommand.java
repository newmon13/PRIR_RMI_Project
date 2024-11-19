package dev.jlipka;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AddCommand implements Command<Integer> {
    private static final String UNIQUE_BINDING_NAME = "simple.calculator";
    private final SimpleCalculator<Integer> simpleCalculator;
    final int a;
    final int b;

    public AddCommand(int a, int b) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(2732);
        Remote remoteObj = registry.lookup(UNIQUE_BINDING_NAME);
        if (remoteObj instanceof SimpleCalculator) {
            @SuppressWarnings("unchecked")
            SimpleCalculator<Integer> calculator = (SimpleCalculator<Integer>) remoteObj;
            this.simpleCalculator = calculator;
        } else {
            throw new IllegalArgumentException("Remote object is not a SimpleCalculator");
        }
        this.a = a;
        this.b = b;
    }

    @Override
    public Integer execute() throws RemoteException {
        return simpleCalculator.add(a, b);
    }
}
