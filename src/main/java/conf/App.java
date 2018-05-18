package conf;



import Control.UserController;
import conf.exception.RuntimeExceptionMapper;
import conf.filter.GsonProvider;
import conf.filter.TestEndpoint;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by pitton on 2017-02-20.
 */
@ApplicationPath("")
public class App extends Application {

    @Override
    public Set<Object> getSingletons() {
        Set<Object> sets = new HashSet<>(1);
        sets.add(new TestEndpoint());
        sets.add(new UserController());
        return sets;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> sets = new HashSet<>(1);
        sets.add(GsonProvider.class);
        sets.add(RuntimeExceptionMapper.class);
        return sets;
    }
}

