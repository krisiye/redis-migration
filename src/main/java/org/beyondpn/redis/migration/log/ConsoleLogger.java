/*
 * Copyright 2015 BeyondPN
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

package org.beyondpn.redis.migration.log;

/**
 * Print logger message to console.
 * <p>
 * Created by yangjianhua on 2015/4/22.
 */
public class ConsoleLogger implements Logger {

    @Override
    public void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    @Override
    public void info(String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    public void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    @Override
    public void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    @Override
    public void error(String message, Exception exception) {
        System.out.println("[DEBUG] " + message);
        exception.printStackTrace();
    }
}
