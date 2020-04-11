package resource

import io.mockk.*
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.PrintWriter
import java.io.StringReader
import java.io.StringWriter
import java.util.*
import java.util.concurrent.CompletableFuture
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ActionsServletTest {

    private lateinit var actionsServlet: ActionsServlet
    private lateinit var req: HttpServletRequest
    private lateinit var res: HttpServletResponse
    private lateinit var mockPrintWriter: PrintWriter

    @Before
    fun setup() {
        unmockkAll()
        mockkConstructor(MyActionsApp::class)
        every { anyConstructed<MyActionsApp>().handleRequest("{}", mapOf<String?, String?>()) } returns CompletableFuture.completedFuture("{}")
        actionsServlet = ActionsServlet()
        mockPrintWriter = PrintWriter(StringWriter())
        req = mockk {
            every { reader } returns BufferedReader(StringReader("{}"))
            every { headerNames } returns mockk { every { hasMoreElements() } returns false }
        }
        res = mockk(relaxed = true) {
            every { writer } returns mockPrintWriter
        }

        spyk(res)
    }

    @Test
    fun testDoPost() {
        actionsServlet.doPost(req, res)

        verify { res.writer }
        verify { res.contentType = "application/json" }
        confirmVerified(res)
    }

    @Test
    fun testDoGet() {
        actionsServlet.doGet(req, res)

        verify { res.writer }
        verify { res.contentType = "text/plain" }
        confirmVerified(res)
    }

    @Test
    fun testHandleError() {
        every { anyConstructed<MyActionsApp>().handleRequest("{}", mapOf<String?, String?>()) } throws Exception()

        actionsServlet.doPost(req, res)

        verify { res.writer }
        confirmVerified(res)
    }

    @Test
    fun testDoPost_WithBody() {
        every { req.headerNames } returns Collections.enumeration(listOf("Content-type"))
        every { req.getHeader("Content-type") } returns "application/json"

        actionsServlet.doPost(req, res)

        verify { res.writer }
        confirmVerified(res)
    }

}