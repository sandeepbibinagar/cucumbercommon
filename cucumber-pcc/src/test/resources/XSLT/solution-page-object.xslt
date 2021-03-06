<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
    <pages>
      <xsl:for-each select="collection(concat('file:///@@@bundles-path@@@', '?select=*.pg.xml;recurse=yes'))">
        <xsl:apply-templates/>
      </xsl:for-each>
    </pages>
  </xsl:template>

  <xsl:template match="page|search-page">

    <page>
      <xsl:attribute name="id">
        <xsl:value-of select="@id"/>
      </xsl:attribute>
      <xsl:attribute name="title">
        <xsl:value-of select="./pagename/text()"/>
      </xsl:attribute>
      <components>
        <xsl:apply-templates select="//sub-menu-item"/>
        <xsl:apply-templates select="//menu-item"/>
        <xsl:apply-templates select="//label"/>
        <xsl:apply-templates select="//dynamic-combo"/>
        <xsl:apply-templates select="//combobox"/>
        <xsl:apply-templates select="//button"/>
        <xsl:apply-templates select="//title"/>
        <xsl:apply-templates select="//textfield"/>
        <xsl:apply-templates select="//jquerydatepicker"/>
      </components>
    </page>

  </xsl:template>

  <xsl:template match="sub-menu-item">
    <component>
      <xsl:attribute name="type">sub-menu-item</xsl:attribute>
      <xsl:attribute name="display-text">
        <xsl:value-of select="@id"/>
      </xsl:attribute>
      <xsl:attribute name="xpath">
        <xsl:text>//a[contains(text(),'</xsl:text><xsl:value-of select="@id"/><xsl:text>')]</xsl:text>
      </xsl:attribute>
    </component>
  </xsl:template>

  <xsl:template match="menu-item">
    <component>
      <xsl:attribute name="type">menu-item</xsl:attribute>
      <xsl:attribute name="display-text">
        <xsl:value-of select="@id"/>
      </xsl:attribute>
      <xsl:attribute name="xpath">
        <xsl:text>//a[contains(text(),'</xsl:text><xsl:value-of select="@id"/><xsl:text>')]</xsl:text>
      </xsl:attribute>
    </component>
  </xsl:template>

  <xsl:template match="label">
    <component>
      <xsl:attribute name="type">label</xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="./text/text()"/>
      </xsl:attribute>
      <xsl:attribute name="xpath">
        <xsl:text>//label[contains(text(),'</xsl:text><xsl:value-of select="./text/text()"/><xsl:text>')]</xsl:text>
      </xsl:attribute>
    </component>
  </xsl:template>

  <xsl:template match="dynamic-combo">
    <component>
      <xsl:attribute name="type">dropdown</xsl:attribute>
      <xsl:if test="preceding-sibling::*[1]/local-name()='label'">
        <xsl:attribute name="display-text">
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/>
        </xsl:attribute>
        <xsl:attribute name="display-xpath"><xsl:text>//label[contains(text(),'</xsl:text>
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/><xsl:text>')]</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="xpath"><xsl:text>//select[@data-origin-id='</xsl:text>
          <xsl:value-of select="@id"/><xsl:text>']</xsl:text>
        </xsl:attribute>
      </xsl:if>
      <xsl:if test="./next-combo-id">
        <xsl:attribute name="display-text">
          <xsl:value-of select="./parameter/text()"/>
        </xsl:attribute>
        <xsl:attribute name="xpath"><xsl:text>//select[@data-origin-id='</xsl:text>
          <xsl:value-of select="@id"/><xsl:text>']</xsl:text>
        </xsl:attribute>
      </xsl:if>

    </component>
  </xsl:template>
  <xsl:template match="combobox">
    <component>
      <xsl:attribute name="type">dropdown</xsl:attribute>
      <xsl:if test="preceding-sibling::*[1]/local-name()='label'">
        <xsl:attribute name="display-text">
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/>
        </xsl:attribute>
        <xsl:attribute name="display-xpath"><xsl:text>//label[contains(text(),'</xsl:text>
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/><xsl:text>')]</xsl:text>
        </xsl:attribute>
      </xsl:if>
      <xsl:attribute name="xpath"><xsl:text>//select[@data-origin-id='</xsl:text>
        <xsl:value-of select="@id"/><xsl:text>']</xsl:text>
      </xsl:attribute>
    </component>
  </xsl:template>

  <xsl:template match="button">
    <component>
      <xsl:attribute name="type">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>
      <xsl:attribute name="xpath"><xsl:text>//button[@data-origin-id='</xsl:text>
        <xsl:value-of select="@id"/><xsl:text>']</xsl:text>
      </xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="@id"/>
      </xsl:attribute>
    </component>
  </xsl:template>

  <xsl:template match="title">
    <component>
      <xsl:attribute name="type">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>
      <xsl:attribute name="xpath"><xsl:text>//*[contains(text(),'</xsl:text>
        <xsl:value-of select="text()"/><xsl:text>')]</xsl:text>
      </xsl:attribute>
      <xsl:attribute name="text">
        <xsl:value-of select="text()"/>
      </xsl:attribute>
    </component>
  </xsl:template>

  <xsl:template match="textfield">
    <component>
      <xsl:attribute name="type">
        <xsl:value-of select="local-name()"/>
      </xsl:attribute>
      <xsl:attribute name="xpath"><xsl:text>//input[@data-origin-id='</xsl:text>
        <xsl:value-of select="@id"/><xsl:text>']</xsl:text>
      </xsl:attribute>
      <xsl:if test="preceding-sibling::*[1]/local-name()='label'">
        <xsl:attribute name="display-text">
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/>
        </xsl:attribute>
        <xsl:attribute name="display-xpath"><xsl:text>//label[contains(text(),'</xsl:text>
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/><xsl:text>')]</xsl:text>
        </xsl:attribute>
      </xsl:if>
    </component>
  </xsl:template>

  <xsl:template match="jquerydatepicker">
    <component>
      <xsl:attribute name="type">datepicker</xsl:attribute>
      <xsl:attribute name="xpath"><xsl:text>//input[@data-origin-id='</xsl:text>
        <xsl:value-of select="@id"/><xsl:text>']</xsl:text>
      </xsl:attribute>
      <xsl:if test="preceding-sibling::*[1]/local-name()='label'">
        <xsl:attribute name="display-text">
          <xsl:value-of select="preceding-sibling::*[1]/text/text()"/>
        </xsl:attribute>
        <xsl:attribute name="display-xpath">//label[contains(text(),'<xsl:value-of
          select="preceding-sibling::*[1]/text/text()"/>')]
        </xsl:attribute>
      </xsl:if>
    </component>
  </xsl:template>

</xsl:stylesheet>
