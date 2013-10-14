<div id="formPage1" style="background-color: #EEE">

  <div style="position: absolute; top: 32px; right: 5px">
    <tmpl:/help/helpIcon id="1331"/>
  </div>

  <div class="dataFormTitle" id="dataFormTitle1">
    <g:if test="${uploadDataInstance?.id ? true : false}">
        <h1>Edit Metadata</h1>
    </g:if>
    <g:else>
        <h1>Upload Data</h1>
    </g:else>
  </div>
  <div style="text-align:right">
    <a class="button" href="mailto:${grailsApplication.config.com.recomdata.dataUpload.adminEmail}">Email administrator</a>
    <div class="uploadMessage">If you are unable to locate the relevant study, email the administrator by clicking the button above.</div>
  </div>
  <g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
  </g:if>

  <table class="uploadTable">
    <tr>
      <td width="10%">&nbsp;</td>
      <td width="90%">&nbsp;
      </td>
    </tr>
    <tr>
      <td>
        Study:
      </td>
      <td>
        <div id="studyErrors">
          <g:eachError bean="${uploadDataInstance}" field="study">
          <div class="fieldError"><g:message error="${it}"/></div>
          </g:eachError>
        </div>
        <tmpl:extSearchField width="600" fieldName="study" searchAction="extSearch" searchController="experiment" value="${study?.accession}" label="${study?.title}"/>
        <a id="studyChangeButton" class="button" onclick="$j('#studyDiv').empty().slideUp('slow'); changeField('study-combobox', 'study')">Change</a>
        <a style="margin-left: 5px;" id="studyBrowseButton" class="button" onclick="generateBrowseWindow('Studies');">Browse</a>
        <br/><br/>
        <div id="studyDiv" style="height: 200px; width: 540px; overflow: auto; display: none;">&nbsp;</div>
      </td>
    </tr>

    <tr>
      <td>
        Analysis Type to Upload<br/>
      </td>
      <td>
        <div id="dataTypeErrors">
          <g:eachError bean="${uploadDataInstance}" field="dataType">
          <div class="fieldError"><g:message error="${it}"/></div>
          </g:eachError>
        </div>
        <g:select name="dataType" name="dataType" noSelection="${['null':'Select...']}" from="${['GWAS':'GWAS','Metabolic GWAS':'GWAS Metabolomics','EQTL':'eQTL']}" optionKey="${{it.key}}" optionValue="${{it.value}}" value="${uploadDataInstance?.dataType}"/>
      </td>
    </tr>

    <tr>
      <td>
        Analysis Name:
      </td>
      <td>
        <div id="analysisNameErrors">
          <g:eachError bean="${uploadDataInstance}" field="analysisName">
          <div class="fieldError"><g:message error="${it}"/></div>
          </g:eachError>
        </div>
        <g:textField name="analysisName" style="width: 90%" value="${uploadDataInstance.analysisName}"/>
      </td>
    </tr>
    <tr>
      <td>Analysis Description:</td>
      <td colspan="3">
        <g:textArea name="description" style="width: 90%; height: 100px">${uploadDataInstance.description}</g:textArea>
      </td>
    </tr>
  </table>

  <div class="buttonbar">
    <a class="button" onclick="showDataUploadForm()">Enter metadata</a>
    <a class="button" href="${createLink([action:'index',controller:'search'])}">Cancel</a>
  </div>
</div>
