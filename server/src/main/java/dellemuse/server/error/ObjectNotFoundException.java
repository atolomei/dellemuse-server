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
package dellemuse.server.error;

import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.ErrorProxy;
import dellemuse.model.error.HttpStatus;

/**
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
public class ObjectNotFoundException extends ServerAPIException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(ErrorProxy proxy) {
        super(proxy);
    }

    public ObjectNotFoundException(dellemuse.model.error.ErrorCode errorCode, String message) {
        super(HttpStatus.NOT_FOUND, errorCode, message);
    }

    public ObjectNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, ErrorCode.OBJECT_NOT_FOUND, message);
    }

}
