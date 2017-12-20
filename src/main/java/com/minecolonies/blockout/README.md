About BlockOut
--------------
BlockOut is a data-driven GUI library for Minecraft.

BlockOut uses a hierarchical structure, consisting of a root Window, which contains one or more Panes and Views.  Views
are a type of Pane which can contain other Panes (and Views).

Position coordinates in BlockOut are relative, where a Pane's x,y position is relative to the top-left of the parent
View.  There are various means of declaring the size and position of a Pane.

***

Usage
-----

### Installation
#### Minecraft Player
If you are using the _Standard_ version of Minecolonies, then you do not need to do anything else. BlockOut is included by default.
However if you are not using that version of Minecolonies (or are using it for a different mod) then you can download a copy from _CurseForge_
#### Minecraft Mod Maker
##### TeamCity Server
To install BlockOut you need to download a deobfuscated copy of the source directly from our [TeamCity](teamcity.minecolonies.com) build system.
Log in as a Guest and get the requirements for the instance of BlockOut you want to use there.
##### Requirements
###### Build Data
Each of our projects has multiple builds that you can use to develop against, so does BlockOut.
The most used builds are `Alpha`, `Beta` and `Release`. If you wish to try a custom variant with specific changes, you may want to check out the `Branches`-Build-type.
Write the Build-Type you desire down (this will mostly be Release), also write the build number down (this specifies which Git-Branch you want to use, and can be seen in the overview directly behind the branch name.)
###### Artifact Data
Next you will need some data about the artifact you want to use. We release multiple artifacts per build and not all are suited to be used in a Development environment.
In TeamCity when you have chosen the Build you want to use, in its row there is a dropdown for artifacts on the right hand side.
All the artifact data can be gathered from the filename. This name is structured as follows: `blockout-{mc_version}-{mod_version}-{artifact_id}-{appendix}.jar`:
 * First and foremost, its appendix (this is usually (SNAPSHOT-)deobf or (SNAPSHOT-)api depending on what you are trying to achieve)(`{appendix}`)
 * Second you need the version that is compiled into the artifact (`{mod_version}`)
 * Third you need the id of the artifact (`{artifact_id}`)
##### build.gradle Additions
###### Repository
Add the following snipped to your `repositories { ... }` section:  
`ivy {`  
`name 'Minecolonies TeamCity Repo'`  
`ivyPattern 'http://teamcity.minecolonies.com/guestAuth/repository/download/[module]/[revision]/teamcity-ivy.xml'`    
`artifactPattern 'http://teamcity.minecolonies.com/guestAuth/repository/download/[module]/[revision]/[artifact](.[ext])'`    
`}`
This adds the Minecolonies build system as a Repository for artifacts to your build system and allows gradle to download artifacts from it.
###### Dependency
Add the following snipped to your `dependencies { ...}` section:  
`deobfCompile ( 'org:BlockOut_' + {your_build_type} + 'Builds:' + {your_build_number} ){`  
`        artifact {`  
`            name = 'blockout-' + {your_mc_version} + '-' + {your_mod_version} +'-' + {your_artifact_id} +'-' + {your_artifact_appendix}`  
`            type = 'jar'` 
`        }`  
`    }`

Replace here by the `{your_*}` elements with the data from the requirements list. A good idea is to put this data into a gradle.properties file and store it there.
Now when you rerun `gradlew setupDecompWorkspace` BlockOut will be added as a Dependency.
### Code
To display a Window, instantiate a Window (or subclass), then call:

    yourWindow.open();

The Window constructor takes a Resource Location path to the Layout file to use for the Window.

If a Window requires no special code, you can directly instantiate `com.blockout.views.Window`.  Otherwise, create a
Java class that extends com.blockout.views.Window.  Override Window methods to add additional behavior, such as click
handling.

If your Window subclass implements com.blockout.controls.ButtonHandler, it will automatically receive button events via
the `onButtonClicked()`


### Layout Files
While you can programmatically create a BlockOut layout, the easiest method is to use a BlockOut layout files, which is
an XML file that describes the layout of the Window.


### Localization
Text in Layout files is automatically localized.  All text that is displayed to the user is parsed for tokens of the
format "$(identifier)" and replaced with matching localized text.

For example, if you have the following localized string in the language assest file:

`com.yourmod.gui.string=Oranges`

then a button defined in a layout file as:

`<button label="100 $(com.yourmod.gui.somestring)"/>`

the button will display the text "100 Oranges".


***

Layout File Definition
----------------------
BlockOut Layout Files are XML files.  There are two root elements supported: `<window>` and `<layout>`.

A Window must reference an XML file that uses a `<window>` root element, while files with a `<layout>` root element are
for embedding via inclusion with the <layout> child element.

`<window size="171 247" pause="false" lightbox="false"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:noNamespaceSchemaLocation="file:../../../../java/com/blockout/blockOut.xsd">`

BlockOut XML files can inherit from other files, or embed them.

The `xmlns:xsi` and `xsi:noNamespaceSchemaLocation` attributes are supported for either root element, and are optional,
but their use allows you to point to the xsd file for editor validation purposes.

#### Resource Location Paths
Resource Location paths use the format: _modid_:path/under/mod/assets

For example: "yourMod:gui/windows/myWindow.xml"

is a path to the file: assets/yourMod/gui/windows/myWindow.xml


#### Supported Colors
When a color can be specified, the following formats are supported:

* `CSS Hex Format:` #00aabbcc
* `CSS RGB and RGBA formats:` rgb(255,0,0) and rgba(255,0,0,0.3)
* `Integer:` 1632768
* `Name:` aqua, black, blue, cyan, fuchsia, green, ivory, lime, magenta, orange, orangered, purple, red, white, yellow,
          gray, darkgray, dimgray, lightgray, slategray


#### Common Attributes
These attributes are supported by all Pane types except the two root elements (<window> and <layout>)

**Generic Attributes**

* `id="{id}"`                     String identifier, for use by the code to access specific elements
* `visible="{true|false}"`        (Default=true) Visibility of a pane, allow a visual element to start hidden
* `enabled="{true|false}"`        (Default=true) Disabled panes do not receive clicks and may render differently
* `style="{style}"`               If additional styles have been registered for a Pane type, this will switch the
                                  style of the Pane

***
**Size Attributes**

* `size="{width} {height}"`       Size pair defining the width and height of the Pane
* `width="{width}"`               Width only
* `height="{height}"`             Height only

__width__ and __height__ values from these attributes may either be a number (e.g, "100" or "100px") or a percentage
(e.g, "10%").  Percentage is treated as a percent of the parent's width or height, respectively.

A negative __width__ or __height__ is treated as 100% of the parent's width or height, minus the absolute value.
For example, if the parent is 80x200 pixels, and the pane has a specified size of "-25 -30%" it will have a size of
55x140: 80-25 x 200-60.

If you use a `size` attribute, do not use a `width` or `height` attribute.

***
**Positioning**

* `align="{align}"`               (Default=TopLeft) Alignment
* `pos="{x} {y}"`                 Position pair defining the x and y offset of the Pane within its parent
* `x="{x}"`                       x position only
* `y="{y}"`                       y position only

__x__ and __y__ values follow the same rules as __width__ and __height__ regarding the numerical values, percentages,
and negative values.

If you use a `pos` attribute, do not use an `x` or `y` attribute.

`align` can be one of the following: TopLeft, TopMiddle, TopRight, MiddleLeft, Middle, MiddleRight, BottomLeft,
BottomMiddle, BottomRight

In the case of *Bottom* and *Right* alignment modes, positioning functions like CSS padding in 'relative' mode, such
that the __x__ and __y__ position values are reversed - in a *Bottom* alignment mode, a positive __y__ will move the
Pane up.  Likewise, in the case of *Right* alignment modes, the __x__ attribute is reversed and moves the Pane left
instead of right.  These rules also apply in the case of text alignment and offset.

***
#### Window root element <window>
Root element for Window layout files.  Does not support the Common attributes.

Window elements can contain any non-root element.

* `size="{width} {height}"`       See 'Size Attributes' under Common Attributes above.  Percentages are not supported.
* `width="{width}"`
* `height="{height}"`
* `pause="true|false"`            (Default=true) Pause the game when the Window is displayed
* `lightbox="true|false"`         (Default=true) Show a dimmed lightbox (like stock GUI)

The default (and maximum) Window size is 420x240.

***
#### Layout root element <layout>
Root element for embeddable layout files.  Layout root elements have no attributes, and exist only as a container.

Layout (root) elements can contain any non-root element.

***
#### View <view>
Views are simple containers, and can contain any non-root element.

Child elements of Views (including Windows) that are entirely outside the View's bounds will not be rendered or receive
clicks.

* `padding="{pad}"`               The usable interior of the View is reduced by the padding on all sides, and elements
                                  are offset by the padding in both directions.

***
#### Group <group>
An automatically Y-sorted list of child elements.

Child elements are automatically re-positioned to be sorted vertically, in the order they appear in the layout file.

* `spacing="{spacing}"`          Y-gap between elements

***
#### List View <list>
A scrolling list, using identical rows.  Features a scroll-bar when the list cannot fit within the bounds of the Pane.

Only the first child element defined in the file (and all of it's children) will be used, as the definition for the
layout of the row.

List views need code support, to provide a ScrollingList.DataProvider which defines the number of rows in the list, and
handles setting up Panes in individual row elements.

* `spacing="{spacing}"`           Y-gap between row elements

***
#### Box <box>
A View which draws a box around it's border.

* `color="{color}"`               Color of the line; see 'Supported Colors', above
* `linewidth="{width}"`           Width of the line, in pixels

***
#### Switch View <switch>
A View which only shows one direct child Pane/View at a time, with code support for switching between them.

* `default="{id}"`                Initial visible child pane.  If none is specified, defaults to the first child.

***
#### Button <button>
A typical Minecraft-style button.

* `label="{text}"`                Text label of the button

***
#### Image Button <buttonimage>
A clickable image which functions like a Button

* `source="{path}"`               **Required** Resource Location path to the texture png to use
* `imageoffset="{x y}"`           Offset of image in source texture to top left of image
* `imagesize="{width height}"`    Size of image in source texture
* `highlight="{path}"`            Path to texture png to use for mouse-over highlight render effect, if desired
* `highlightoffset="{x y}"`       Offset of image in highlight texture to top left of image
* `highlightsize="{width height}"` Size of highlight image in source texture
* `disabled="{path}"`             Path to texture png to use for disabled button render effect, if desired
* `disabledoffset="{x y}"`        Offset of image in disabled texture to top left of image
* `disabledsize="{width height}"` Size of disabled image in source texture
* `label="{text}"`                Text label for the button, if desired
* `textscale="{scale}"`           Scale of text for label
* `textalign="{align}"`           (Default=Middle) Alignment of the text label over the image
* `textoffset="{x y}"`            Offset of label for precise positioning
* `textcolor="{color}"`           Color of the label; see 'Supported Colors', above
* `texthovercolor="{color}"`      Color of the label on mouse-over; see 'Supported Colors', above
* `textdisabledcolor="{color}"`   Color of the label when button is disabled; see 'Supported Colors', above
* `shadow="{true|false}"`         Whether to render the label with a shadow

***
#### Label <label>
A basic single-line text label.

* `label="{text}"`                Text label of the button
* `textscale="{scale}"`           Scale of text for label
* `textalign="{align}"`           (Default=MiddleLeft) Alignment of the text within the bounds of the pane
* `color="{color}"`               Color of the label; see 'Supported Colors', above
* `hovercolor="{color}"`          Color of the label on mouse-over; see 'Supported Colors', above
* `shadow="{true|false}"`         Whether to render the label with a shadow

***
#### Text <text>
A multi-line wrapping text box.  The actual text is not an attribute but within the <text></text> element itself.

* `linespace="{space}"`           Additional line spacing
* `textscale="{scale}"`           Scale of the text
* `textalign="{align}"`           (Default=MiddleLeft) Alignment of the text within the bounds of the pane
* `color="{color}"`               Color of the text; see 'Supported Colors', above
* `shadow="{true|false}"`         Whether to render the label with a shadow

***
#### Input <input>

* `text="{text}"`                 Initial text, if any
* `maxlength="{length}"`          Maximum length of the text
* `color="{color}"`               Color of the text; see 'Supported Colors', above
* `colordisabled="{color}"`       Color of the text if the pane is disabled; see 'Supported Colors', above
* `shadow="{true|false}"`         Whether to render the text with a shadow
* `tab="{id}"`                    If the user presses 'tab', the next input pane to switch input to

***
#### Image <image>
A basic image.

* `source="{path}"`               **Required** Resource Location path to the texture png to use
* `imageoffset="{x y}"`           Offset of image in source texture to top left of image
* `imagesize="{width height}"`    Size of image in source texture

***
#### Item Icon / Stack <itemicon>
An item icon or stack

* `item="{item}"`                 Item name identifier (e.g, "gold_ingot")

***
#### Layout child element <layout>
Layout child elements allow embedding the contents of other XML files directly, as if the contents under the <layout>
root element in the embedded file existed in place of the <layout> child element in the embedding file.

* `source="{path}"`               Resource Location path to the XML layout file to embed.