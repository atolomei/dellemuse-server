
package dellemuse.server.traffic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.server.BaseService;
import dellemuse.server.ServiceStatus;
import dellemuse.server.Settings;


/**
 * 
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
@Service
public class TrafficControlService extends BaseService {


    static private Logger logger = Logger.getLogger(TrafficControlService.class.getName());
    static private Logger startuplogger = Logger.getLogger("StartupLogger");

    @JsonIgnore
    @Autowired
    private final Settings serverSettings;

    @JsonIgnore
    private Set<IToken> passes = null;

    @JsonProperty("waittimeout")
    private long waittimeout = 10000L;

    @JsonProperty("tokens")
    private int tokens = Constant.TRAFFIC_TOKENS_DEFAULT;



    public TrafficControlService(Settings settings) {
        super(settings);
        this.serverSettings=settings;
    }

    public IToken getPass() {

        IToken pass = null;
        long initialtime = System.currentTimeMillis();
        long wait = 0;
        boolean inqueue = false;

        try {
            while (pass == null) {

                synchronized (this) {
                    if (!passes.isEmpty()) {
                        pass = passes.iterator().next();
                        passes.remove(pass);
                    }
                }

                if (pass == null) {
                    wait = System.currentTimeMillis() - initialtime;
                    if (wait > waittimeout) {
                        logger.error("TimeoutException  | passes = " + String.valueOf(passes));
                        throw new RuntimeException("TimeoutException  | passes -> " + String.valueOf(passes));
                    }

                    synchronized (this) {
                        try {
                            if (!inqueue) {
                                inqueue = true;
                            }
                            wait(500);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        } finally {
        }
        return pass;
    }

    public void release(IToken pass) {

        if (pass == null)
            return;

        synchronized (this) {
            this.passes.add(pass);
            notify();
        }
    }

    public void setTimeout(long value) {
        waittimeout = value;
    }

    @PostConstruct
    protected synchronized void onInitialize() {
        setStatus(ServiceStatus.STARTING);
        this.tokens = serverSettings.getMaxTrafficTokens();
        createPasses();
        setStatus(ServiceStatus.RUNNING);
        startuplogger.debug("Started -> " + TrafficControlService.class.getSimpleName());
    }

    protected synchronized void createPasses() {
        this.passes = Collections.synchronizedSet(new HashSet<IToken>(this.tokens));
        for (int n = 0; n < this.tokens; n++)
            passes.add(new TrafficToken(n));
    }
}
