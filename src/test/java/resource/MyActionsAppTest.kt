package resource

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull

class MyActionsAppTest {

    @Test
    fun testConstuctor() {
        val actionsApp = MyActionsApp()

        assertNotNull(actionsApp)
    }

}