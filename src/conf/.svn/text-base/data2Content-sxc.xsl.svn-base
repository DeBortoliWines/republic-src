<?xml version="1.0"?>
<xsl:stylesheet
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ns="http://www.w3.org/TR/REC-html140"
	xmlns:office="http://openoffice.org/2000/office"
	xmlns:style="http://openoffice.org/2000/style"
	xmlns:text="http://openoffice.org/2000/text"
	xmlns:table="http://openoffice.org/2000/table"
	xmlns:draw="http://openoffice.org/2000/drawing"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:number="http://openoffice.org/2000/datastyle"
	xmlns:svg="http://www.w3.org/2000/svg"
	xmlns:chart="http://openoffice.org/2000/chart"
	xmlns:dr3d="http://openoffice.org/2000/dr3d"
	xmlns:math="http://www.w3.org/1998/Math/MathML"
	xmlns:form="http://openoffice.org/2000/form"
	xmlns:script="http://openoffice.org/2000/script"	
	>

	<xsl:template
		match="/">
		<office:document-content
			xmlns:office="http://openoffice.org/2000/office"
			xmlns:style="http://openoffice.org/2000/style"
			xmlns:text="http://openoffice.org/2000/text"
			xmlns:table="http://openoffice.org/2000/table"
			xmlns:draw="http://openoffice.org/2000/drawing"
			xmlns:fo="http://www.w3.org/1999/XSL/Format"
			xmlns:xlink="http://www.w3.org/1999/xlink"
			xmlns:number="http://openoffice.org/2000/datastyle"
			xmlns:svg="http://www.w3.org/2000/svg"
			xmlns:chart="http://openoffice.org/2000/chart"
			xmlns:dr3d="http://openoffice.org/2000/dr3d"
			xmlns:math="http://www.w3.org/1998/Math/MathML"
			xmlns:form="http://openoffice.org/2000/form"
			xmlns:script="http://openoffice.org/2000/script"
			office:class="spreadsheet"
			office:version="1.0">
			<office:script />
			<office:font-decls>
				<style:font-decl
					style:name="Andale Sans UI"
					fo:font-family="'Andale Sans UI'"
					style:font-pitch="variable" />
				<style:font-decl
					style:name="Lucida Sans Unicode"
					fo:font-family="'Lucida Sans Unicode'"
					style:font-pitch="variable" />
				<style:font-decl
					style:name="Lucidasans"
					fo:font-family="Lucidasans"
					style:font-pitch="variable" />
				<style:font-decl
					style:name="Tahoma"
					fo:font-family="Tahoma"
					style:font-pitch="variable" />
				<style:font-decl
					style:name="Albany"
					fo:font-family="Albany"
					style:font-family-generic="swiss"
					style:font-pitch="variable" />
				<style:font-decl
					style:name="Arial"
					fo:font-family="Arial"
					style:font-family-generic="swiss"
					style:font-pitch="variable" />
			</office:font-decls>
			<office:automatic-styles>
				<style:style
					style:name="co1"
					style:family="table-column">
					<style:properties
						fo:break-before="auto"
						style:column-width="2.686cm" />
				</style:style>
				<style:style
					style:name="co2"
					style:family="table-column">
					<style:properties
						fo:break-before="auto"
						style:column-width="5.225cm" />
				</style:style>
				<style:style
					style:name="co3"
					style:family="table-column">
					<style:properties
						fo:break-before="auto"
						style:column-width="2.267cm" />
				</style:style>
				<style:style
					style:name="ro1"
					style:family="table-row">
					<style:properties
						style:row-height="0.427cm"
						fo:break-before="auto"
						style:use-optimal-row-height="true" />
				</style:style>
				<style:style
					style:name="ta1"
					style:family="table"
					style:master-page-name="Default">
					<style:properties
						table:display="true" />
				</style:style>
				<style:style
					style:name="ce1"
					style:family="table-cell"
					style:parent-style-name="Default">
					<style:properties
						fo:font-style="italic"
						fo:font-weight="bold" />
				</style:style>
				<style:style
					style:name="ce2"
					style:family="table-cell"
					style:parent-style-name="Default"
					style:data-style-name="N107" />
			</office:automatic-styles>

								
			<office:body>
				
			<xsl:for-each
					select="output-data/outputDataBeans/outputDataBean">		
			
					<xsl:variable name="databaseName" select="data-base-name"/>					

				<table:table
					table:name="{$databaseName}"
					table:style-name="ta1">
					<table:table-column
						table:style-name="co1"
						table:default-cell-style-name="Default" />
					<table:table-column
						table:style-name="co2"
						table:default-cell-style-name="Default" />
					
					<table:table-column
						table:style-name="co3"
						table:number-columns-repeated="2"
						table:default-cell-style-name="ce2" />

					<!-- Build Column Names -->

					<table:table-row
						table:style-name="ro1">

						<xsl:for-each
							select="outputDataColumnNames/outputDataColumnName">

							<table:table-cell
								table:style-name="ce1">
								<text:p>

									<xsl:value-of
										select="." />

								</text:p>
							</table:table-cell>

						</xsl:for-each>

					</table:table-row>

					<!-- Now to Extract the Data from the datafile
						This builds the Cells -->
					<xsl:for-each
						select="outputDataRows/outputDataRow">

						<table:table-row
							table:style-name="ro1">

							<xsl:for-each
								select="outputDataColumns/outputDataColumn">

								<xsl:if
									test="(column-field-type = 'string')">
									<table:table-cell
										table:style-name="ce2">
										<text:p>
											<xsl:value-of
												select="column-value" />
										</text:p>
									</table:table-cell>
								</xsl:if>
								<xsl:if	test="(column-field-type = 'currency')">
									<xsl:call-template name="makeCurrencyCell">
											<xsl:with-param name="currency"><xsl:value-of select="column-value"/></xsl:with-param>         
									</xsl:call-template>
								</xsl:if>	
								
							    <xsl:if test="(column-field-type = 'number')">
									
									<xsl:variable
										name="curr"
										select="column-value" />
									
									<table:table-cell
										table:value-type="float"
										table:value="{$curr}">
										<text:p>
											<xsl:value-of
												select="column-value" />
										</text:p>
									</table:table-cell>
								</xsl:if>				

                                <xsl:if test="(column-field-type = 'percentage')">
									<xsl:call-template name="makePercentageCell">
										<xsl:with-param name="percentage"><xsl:value-of select="column-value"/></xsl:with-param>         
									</xsl:call-template>				
								</xsl:if>

								<xsl:if test="(column-field-type = 'date')">
									<xsl:variable name="dateval" select="column-value"/>
									<xsl:variable name="oodatevalyy" select="substring($dateval,7,2)"/>
									<xsl:variable name="oodatevalmm" select="substring($dateval,4,2)"/>
									<xsl:variable name="oodatevaldd" select="substring($dateval,1,2)"/>
									
									<table:table-cell  table:value-type="date" 
										table:date-value="20{$oodatevalyy}-{$oodatevalmm}-{$oodatevaldd}"  
										table:style-name="ce2">
								
									</table:table-cell>		  

								</xsl:if>
								
							</xsl:for-each>

						</table:table-row>

					</xsl:for-each>
					<!-- End of Extract Data from the datafile -->

				</table:table>

			<!-- FOR EACH SHEET -->
			</xsl:for-each>				
				
			</office:body>

		</office:document-content>
	</xsl:template>
	<xsl:template name="makeCurrencyCell">    
		<xsl:param name="currency"/>
			<table:table-cell
				table:value-type="currency"
				table:currency="USD"
				table:value="{$currency}"
				table:style-name="ce4">

			</table:table-cell>
	</xsl:template>

	<xsl:template name="makePercentageCell">    
		<xsl:param name="percentage"/>
		
		<xsl:variable name="decpct"><xsl:value-of select="($percentage div 100)"/>	
		</xsl:variable>
		<xsl:variable name="decpctdispt"><xsl:value-of select="$percentage"/>	
		</xsl:variable>

		<table:table-cell
			table:value-type="percentage"
			table:value="{$decpct}"
			table:style-name="N11">
			<text:p><xsl:value-of select="$decpctdispt"/></text:p>
		</table:table-cell>

	</xsl:template>
	
	
</xsl:stylesheet>
