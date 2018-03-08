<?xml version="1.0"?>
<xsl:stylesheet
	version="1.1"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ns="http://www.w3.org/TR/REC-html140"
	xmlns:office="http://openoffice.org/2000/office"
	xmlns:table="http://openoffice.org/2000/table">
	<xsl:template
		match="/">
		<outputDataBean>
			<outputDataRows>
				<xsl:for-each
					select="/office:document-content/office:body/table:table/table:table-row">
					<outputDataRow>
						<outputDataColumns>
							<xsl:for-each
								select="table:table-cell">
								<outputDataColumn>
									<column-field-type>
										string
									</column-field-type>
									<column-value>
										<xsl:value-of
											select="." />
									</column-value>
								</outputDataColumn>
							</xsl:for-each>
						</outputDataColumns>
					</outputDataRow>
				</xsl:for-each>
			</outputDataRows>
		</outputDataBean>
	</xsl:template>

</xsl:stylesheet>
