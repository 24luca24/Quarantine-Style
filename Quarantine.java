import java.util.function.Function;
import java.util.function.Supplier;

public class Quarantine <T> {
    
    //instance variable
    private final Supplier<T> fnToRun;

    //constructor take a Supplier (takes nothing and return a value)
    public Quarantine(Supplier<T> fnToRun) {
        this.fnToRun = fnToRun;
    }

    //map -> accept a function that is applied to a quarantine value. Unwrap the object inside the  
    //quarantine object, apply the function to it, and then wrap the result back up in a new quarantine object.
    public <R> Quarantine<R> map(Function<T, R> f) {
        Supplier<R> newFnToRun = () -> f.apply(this.run());
        return new Quarantine<>(newFnToRun);
    }

    //bind -> takes a fucntion that return a quarantine object. Unwrap the object inside the quarantine object,
    //apply the function to it, and then return the result inside a quarantine object.
    public <R> Quarantine<R> bind(Function<T, Quarantine<R>> f) {
        Supplier<R> newFnToRun = () -> f.apply(this.run()).run();
        return new Quarantine<>(newFnToRun);
    }

    //run -> return the value inside the quarantine object, by calling the Supplier's get method.
    public T run() {
        return fnToRun.get();
    }
}
