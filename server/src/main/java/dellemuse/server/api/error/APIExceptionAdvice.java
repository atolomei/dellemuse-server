/*
 * Odilon Object Storage
 * (C) Novamens 
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
package dellemuse.server.api.error;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import dellemuse.model.error.ErrorCode;
import dellemuse.model.error.ErrorProxy;
import dellemuse.model.logging.Logger;
import dellemuse.server.error.ServerAPIException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * API Exception controller
 * </p>
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 */
@ControllerAdvice
public class APIExceptionAdvice {

    static private Logger logger = Logger.getLogger(APIExceptionAdvice.class.getName());

    @ExceptionHandler(ServerAPIException.class)
    public ResponseEntity<ErrorProxy> odilonExceptionHandler(ServerAPIException ex) {

        ResponseEntity<ErrorProxy> response = new ResponseEntity<ErrorProxy>(
                new ErrorProxy(ex.getHttpsStatus(), ex.getErrorCode(), ex.getErrorMessage()),
                HttpStatus.valueOf(ex.getHttpsStatus()));
        return response;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorProxy> handle(Exception ex) {

        logger.debug("Server error -> " + ex.getClass().getName() + " | msg: " + ex.getMessage() + " | cause: " + ex.getCause());

        if (ex instanceof NullPointerException) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        ErrorProxy p = null;

        if (ex instanceof org.springframework.web.multipart.MultipartException) {
            p = new ErrorProxy(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.INTERNAL_MULTIPART_ERROR.value(),
                    ex.getClass().getName() + " | msg: " + ex.getMessage() + " | cause: " + ex.getCause());
        } else {
            p = new ErrorProxy(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.INTERNAL_ERROR.value(),
                    ex.getClass().getName() + " | " + ex.getMessage());
        }

        return  new ResponseEntity<ErrorProxy>(p, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
