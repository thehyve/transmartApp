<div id="formPage2" style="background-color: #EEE; display: none;">

  <div style="position: absolute; top: 32px; right: 5px">
    <tmpl:/help/helpIcon id="1332"/>
  </div>

  <div class="dataFormTitle" id="dataFormTitle2">Upload Data</div>
  <div style="text-align:right">
    <a class="button" href="mailto:${grailsApplication.config.com.recomdata.dataUpload.adminEmail}">Email administrator</a>
    <div class="uploadMessage">If you are unable to locate the relevant autocomplete fields, email the administrator by clicking the button above.</div>
  </div>

  <table class="uploadTable">
    <tr>
      <td width="10%">
        File:
      </td>
      <td colspan="3">
        <g:if test="${uploadDataInstance.status == 'ERROR' || uploadDataInstance.status == 'NEW'}">
        <input type="file" id="file" name="file" style="border: 1px dotted #CCC" />
        <a class="upload" href="#" onclick="downloadTemplate();">Download Template</a>
        </g:if>
        <g:else>
        <i>This data set is ${uploadDataInstance.status} - the file cannot be changed.</i>
        </g:else>
        <br/>
        <i>Upload should be a tab-delimited plain text file</i>
      </td>
    </tr>
    <%-- Disabled instant check
    <tr>
      <td>
        &nbsp;
      </td>
      <td colspan="3">
        <div id="columnsAll">&nbsp;</div>
        <div id="columnsNotFound">&nbsp;</div>
        <br/>
      </td>
    </tr>
    --%>

    <tr class="borderbottom bordertop">
      <td id="tagsLabel">
        Phenotype:
      </td>
      <td colspan="3">
        <tmpl:extTagSearchField fieldName="tags" searchAction="extSearch" searchController="disease" values="${tags}"/>
        <%--<a id="tagsLink" class="upload" href="#">Add more Phenotypes/Tags</a>--%>
      </td>
    </tr>
    <tr>
      <td>Population:</td>
      <td>
        <g:textField name="population" value="${uploadDataInstance.population}"/>
      </td>
      <td>Sample Size:</td>
      <td>
        <g:textField name="sampleSize" value="${uploadDataInstance.sampleSize}"/>
      </td>
    </tr>
    <tr>
      <td>Tissue:</td>
      <td>
        <g:textField name="tissue" value="${uploadDataInstance.tissue}"/>
      </td>
      <td>Cell Type:</td>
      <td>
        <g:textField name="cellType" value="${uploadDataInstance.cellType}"/>
      </td>
    </tr>
    <tr class="bordertop borderbottom">
      <td id="platformLabel">
        Platform:
      </td>
      <td colspan="3">
        <div style="width: 100%" id="genotypePlatform-tags" class="tagBox" name="genotypePlatform">
          <g:each in="${genotypePlatforms}" var="value">
          <span class="tag" id="genotypePlatform-tag-${value.key}" name="${value.key}">${value.value}</span>
          </g:each>
        </div>
        <div class="breaker">&nbsp;</div>
        <div style="background-color: #E4E4E4; float:left; padding: 8px; border-radius: 8px;">
          <div style="float: left; font-style: italic; line-height: 32px; margin-right: 8px">Add new: </div>
          <div style="float: left; margin-right: 8px">
            <div class="textsmaller">Vendor</div>
            <g:select style="width: 400px" name="genotypePlatformVendor" noSelection="${['null':'Select...']}" from="${snpVendors}" onChange="loadPlatformTypes('genotypePlatform', 'SNP')"/>
          </div>
          <div style="float: left">
            <div class="textsmaller">Platform</div>
            <g:select style="width: 200px" from="${genotypePlatforms}" name="genotypePlatformName" onchange="addPlatform('genotypePlatform')"/>
            <select id="genotypePlatform" name="genotypePlatform" multiple="multiple" style="display: none;">
              <g:each in="${genotypePlatforms}" var="value">
              <option selected="selected" value="${value.key}">${value.value}</option>
              </g:each>
            </select>
          </div>
        </div>
        <div class="breaker">&nbsp;</div>
      </td>
    </tr>
    <tr>
      <td>
        Genome Version:
      </td>
      <td colspan="3">
        <g:select name="genomeVersion" noSelection="${['null':'Select...']}" from="${['HG18':'HG18','HG19':'HG19']}" optionKey="${{it.key}}" optionValue="${{it.value}}" value="${uploadDataInstance.genomeVersion}"/>
      </td>
    </tr>
    <tr id="expressionPlatformRow" class="bordertop borderbottom">
      <td>
        Expression Platform:
      </td>
      <td colspan="3">
        <div style="width: 100%" id="expressionPlatform-tags" class="tagBox" name="expressionPlatform">
          <g:each in="${expressionPlatforms}" var="value">
          <span class="tag" id="expressionPlatform-tag-${value.key}" name="${value.key}">${value.value}</span>
          </g:each>
        </div>
        <div class="breaker">&nbsp;</div>
        <div style="background-color: #E4E4E4; float:left; padding: 8px; border-radius: 8px;">
          <div style="float: left; font-style: italic; line-height: 32px; margin-right: 8px">Add new: </div>
          <div style="float: left; margin-right: 8px">
            <div class="textsmaller">Vendor</div>
            <g:select style="width: 400px" name="expressionPlatformVendor" noSelection="${['null':'Select...']}" from="${expVendors}" onChange="loadPlatformTypes('expressionPlatform', 'Gene Expression')"/>
          </div>
          <div style="float: left">
            <div class="textsmaller">Platform</div>
            <g:select style="width: 200px" from="${expressionPlatforms}" name="expressionPlatformName" onchange="addPlatform('expressionPlatform')"/>
            <select id="expressionPlatform" name="expressionPlatform" multiple="multiple" style="display: none;">
              <g:each in="${expressionPlatforms}" var="value">
              <option selected="selected" value="${value.key}">${value.value}</option>
              </g:each>
            </select>
          </div>
        </div>
        <div class="breaker">&nbsp;</div>
      </td>
    </tr>
    <tr>
      <td>
        Model Name:
      </td>
      <td>
        <g:textField name="modelName" value="${uploadDataInstance.modelName}"/>
      </td>
      <td>
        Model Description:
      </td>
      <td>
        <g:textField name="modelDescription" value="${uploadDataInstance.modelDescription}"/>
      </td>
    </tr>
    <tr class="borderbottom">
      <td>
        Statistical Test:
      </td>
      <td>
        <g:textField name="statisticalTest" value="${uploadDataInstance.statisticalTest}"/>
      </td>
      <td>
        P-value cutoff &lt;=
      </td>
      <td>
        <g:textField name="pvalueCutoff" value="${uploadDataInstance.pvalueCutoff}"/>
      </td>
    </tr>
    <tr class="borderbottom">
      <td>
        Research Unit:
      </td>
      <td colspan="3">
        <g:textField name="researchUnit" value="${uploadDataInstance.researchUnit}"/>
      </td>
    </tr>
  </table>
  <div class="buttonbar">
    <a class="button" onclick="showAnalysisForm()">Back</a>
    <g:actionSubmit class="upload" value="Upload" action="upload"/>
    <a class="button" href="${createLink([action:'index',controller:'RWG'])}">Cancel</a>
  </div>
</div>
