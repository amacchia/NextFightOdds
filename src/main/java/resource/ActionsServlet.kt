/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package resource

import com.google.actions.api.App
import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Handles request received via HTTP POST and delegates it to your Actions app. See: [Request
 * handling in Google App
 * Engine](https://cloud.google.com/appengine/docs/standard/java/how-requests-are-handled).
 */
@WebServlet(name = "actions", value = ["/"])
class ActionsServlet : HttpServlet() {
    private val actionsApp: App = MyActionsApp()

    @Throws(IOException::class)
    public override fun doPost(req: HttpServletRequest, res: HttpServletResponse) {
        val body = req.reader.lines().collect(Collectors.joining())

        try {
            val headersMap = getHeadersMap(req)
            val jsonResponse = actionsApp.handleRequest(body, headersMap).get()
            res.contentType = "application/json"
            writeResponse(res, jsonResponse)
        } catch (e: Exception) {
            handleError(res, e)
        }
    }

    @Throws(IOException::class)
    public override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        val getResponse = "ActionsServlet is listening but requires valid POST request to respond with Action response."
        response.contentType = "text/plain"
        response
                .writer
                .println(getResponse)
    }

    private fun writeResponse(res: HttpServletResponse, asJson: String) {
        res.writer.write(asJson)
    }

    private fun handleError(res: HttpServletResponse, throwable: Throwable) {
        throwable.printStackTrace()
        res.writer.write("Error handling the intent - " + throwable.message)
    }

    private fun getHeadersMap(request: HttpServletRequest): Map<String?, String?> {
        val map: MutableMap<String?, String?> = HashMap()
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val key = headerNames.nextElement() as String
            val value = request.getHeader(key)
            map[key] = value
        }
        return map
    }

}