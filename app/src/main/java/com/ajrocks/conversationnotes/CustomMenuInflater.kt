import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.XmlResourceParser
import android.util.AttributeSet
import android.util.Xml
import android.view.InflateException
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.annotation.MenuRes
import io.ktor.utils.io.errors.IOException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

class MintActionBarMenu : Menu {
    override fun add(title: CharSequence?): MenuItem {
        TODO("Not yet implemented")
    }

    override fun add(titleRes: Int): MenuItem {
        TODO("Not yet implemented")
    }

    override fun add(groupId: Int, itemId: Int, order: Int, title: CharSequence?): MenuItem {
        TODO("Not yet implemented")
    }

    override fun add(groupId: Int, itemId: Int, order: Int, titleRes: Int): MenuItem {
        TODO("Not yet implemented")
    }

    override fun addSubMenu(title: CharSequence?): SubMenu {
        TODO("Not yet implemented")
    }

    override fun addSubMenu(titleRes: Int): SubMenu {
        TODO("Not yet implemented")
    }

    override fun addSubMenu(groupId: Int, itemId: Int, order: Int, title: CharSequence?): SubMenu {
        TODO("Not yet implemented")
    }

    override fun addSubMenu(groupId: Int, itemId: Int, order: Int, titleRes: Int): SubMenu {
        TODO("Not yet implemented")
    }

    override fun addIntentOptions(
        groupId: Int,
        itemId: Int,
        order: Int,
        caller: ComponentName?,
        specifics: Array<out Intent>?,
        intent: Intent?,
        flags: Int,
        outSpecificItems: Array<out MenuItem>?
    ): Int {
        TODO("Not yet implemented")
    }

    override fun removeItem(id: Int) {
        TODO("Not yet implemented")
    }

    override fun removeGroup(groupId: Int) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun setGroupCheckable(group: Int, checkable: Boolean, exclusive: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setGroupVisible(group: Int, visible: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setGroupEnabled(group: Int, enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun hasVisibleItems(): Boolean {
        TODO("Not yet implemented")
    }

    override fun findItem(id: Int): MenuItem {
        TODO("Not yet implemented")
    }

    override fun size(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(index: Int): MenuItem {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun performShortcut(keyCode: Int, event: KeyEvent?, flags: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun isShortcutKey(keyCode: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    override fun performIdentifierAction(id: Int, flags: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun setQwertyMode(isQwerty: Boolean) {
        TODO("Not yet implemented")
    }

}

class CustomMenuInflater(val context: Context) {

    fun parseMenu(parser: XmlPullParser, attrs: AttributeSet, menu: Menu) {

        var eventType = parser.eventType
        var tagName: String
        var lookingForEndOfUnknownTag = false
        var unknownTagName: String? = null

        // This loop will skip to the menu start tag

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.name
                if (tagName == "menu") {
                    // Go to next tag
                    eventType = parser.next()
                    break
                }
                throw RuntimeException("Expecting menu, got $tagName")
            }
            eventType = parser.next()
        } while (eventType != XmlPullParser.END_DOCUMENT)

        var reachedEndOfMenu = false
        while (!reachedEndOfMenu) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (lookingForEndOfUnknownTag) {
                        break
                    }
                    tagName = parser.name
                    when (tagName) {
                        "group" -> {
                            throw RuntimeException("Group tags are not allowed in mint action bar")
                        }

                        "item" -> {
                            // read menu item here
                            // menuState.readItem(attrs)
                        }

                        "menu" -> {
                            // A menu start tag denotes a submenu for an item
                            throw RuntimeException("Sub menus are not allowed in mint action bar")
                        }

                        else -> {
                            lookingForEndOfUnknownTag = true
                            unknownTagName = tagName
                        }
                    }
                }

                XmlPullParser.END_TAG -> {
                    tagName = parser.name
                    if (lookingForEndOfUnknownTag && tagName == unknownTagName) {
                        lookingForEndOfUnknownTag = false
                        unknownTagName = null
                    } else if (tagName == "item") {
                        // Add the item if it hasn't been added (if the item was
                        // a submenu, it would have been added already)
                    } else if (tagName == "menu") {
                        reachedEndOfMenu = true
                    }
                }

                XmlPullParser.END_DOCUMENT -> throw RuntimeException("Unexpected end of document")
            }
            eventType = parser.next()
        }
    }

    @SuppressLint("ResourceType")
    fun inflate(@MenuRes menuRes: Int, menu: Menu) {
        var parser: XmlResourceParser? = null;
        try {
            parser = context.resources.getLayout(menuRes)
            val attrs: AttributeSet = Xml.asAttributeSet(parser)

            parseMenu(parser, attrs, menu);
        } catch (e: XmlPullParserException) {
            throw InflateException("Error inflating menu XML", e);
        } catch (e: IOException) {
            throw InflateException("Error inflating menu XML", e);
        } finally {
            parser?.close()
        }
    }
}
