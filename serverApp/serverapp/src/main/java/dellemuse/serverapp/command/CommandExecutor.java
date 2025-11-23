/*
 * Odilon Object Storage
 * (c) kbee 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dellemuse.serverapp.command;

import java.time.OffsetDateTime;

import io.odilon.log.Logger;
 

/**
 * <p>
 * This is a Thread that executes a ServiceRequest
 * </p>
 * <p>
 * The Thread that executes the Request is one of the threads from the
 * Dispatcher's pool
 * </p>
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public class CommandExecutor implements Runnable {

    static private Logger logger = Logger.getLogger(CommandExecutor.class.getName());

    private Command command;
    private Dispatcher dispatcher;

    private boolean success = false;

    public CommandExecutor(Command c, Dispatcher dispatcher) {
        this.command = c;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try {

            this.command.setOffsetDateTimeStart(OffsetDateTime.now());
            this.command.setStatus(CommandStatus.RUNNING);
            this.command.execute();
            // this.success = this.command.isSuccess();
        } catch (Throwable e) {

        	//logger.error(e, SharedConstant.NOT_THROWN);
            //this.request.setStatus(ServiceRequestStatus.ERROR);
            //this.success = false;
        } finally {
            try {
          	   this.command.setStatus(CommandStatus.TERMINATED);

          	   
            	// this.request.setEnd(OffsetDateTime.now());

               // if (this.success)
                //    this.schedulerWorker.close(this.request);
               // else
                //    this.schedulerWorker.fail(this.request);


            } catch (Throwable e) {
               // logger.error(e, SharedConstant.NOT_THROWN);
                //try {
                //    this.request.setStatus(ServiceRequestStatus.ERROR);
                //    this.schedulerWorker.fail(this.request);
                //} catch (Exception e1) {
                //   logger.error(e1, SharedConstant.NOT_THROWN);
                //}
            }
        }
    }

    public void setDispatcher(Dispatcher d) {
        this.dispatcher = d;
    }
}
