<%
    config.require("formFieldName")
    config.require("conceptId")
    config.require("maxResults")
%>

<script type="text/javascript">
    jq(function() {
        var xhr=null;//used to track active ajax requests
        
        jq('#${ config.id }-field').autocomplete({
            source: function(request, response) {
                var ajaxUrl = '${ ui.actionLink("registrationapp", "personAttributeWithConcept", "getConcepts")}';
                if(xhr){
                    xhr.abort();
                    xhr = null;
                }
                xhr= jq.ajax({
                    url: ajaxUrl,
                    dataType: 'json',
                    data: { term: request.term , maxResults: ${config.maxResults} ? ${config.maxResults} : 10, conceptId: ${config.conceptId} } ,
                    success: function (data) {
                        if (data.length == 0){
                            data.push({
                               value: 0,
                               label: '${ ui.message("emr.patient.notFound")}'
                            });
                        }
                        
                        response(data);
                    }
                }).complete(function(){
                     xhr = null;
                }).error(function(){
                     xhr = null;
                     console.log("error on searching for patients");
                });
            },
            autoFocus: false,
            minLength: 1,
            delay: 300
        });
    });
</script>

<p <% if (config.left) { %> class="left" <% } %> >

    <label for="${ config.id }-field">
        ${ config.label } <% if (config.classes && config.classes.contains("required")) { %><span>(${ ui.message("emr.formValidation.messages.requiredField.label") })</span><% } %>
    </label>

    <input type="text" id="${ config.id }-field" name="${ config.formFieldName }" value="${ config.initialValue ?: '' }" size="40"
           <% if (config.classes) { %>class="${ config.classes.join(' ') }" <% } %> />

	${ ui.includeFragment("uicommons", "fieldErrors", [ fieldName: config.formFieldName ]) }
    <% if (config.optional) { %>
    ${ ui.message("emr.optional") }
    <% } %>
</p>