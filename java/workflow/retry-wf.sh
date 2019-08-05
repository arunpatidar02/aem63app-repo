#!/bin/bash 

AEM_SCHEME=http
AEM_HOST=localhost
AEM_PORT=4504
AEM_LOGIN=admin:admin
CURL=$(which curl)
REFERER=${AEM_SCHEME}://${AEM_HOST}:${AEM_PORT}
WF_SERVLET=/bin/workflow/inbox
SERVICE_TOKEN=/libs/granite/csrf/token.json
AEM_TOKEN="$(${CURL} -s -H User-Agent:curl -H Referer:${REFERER} -u ${AEM_LOGIN} ${REFERER}${SERVICE_TOKEN} | sed 's/token//' | sed 's/[{":"}]//g')"
WORKFLOW_ITEM=/var/workflow/instances/server0/2019-07-28/request_copy_2/workItems/node1_var_workflow_instances_server0_2019-07-28_request_copy_2

COMMENT='Retry by Admin'

#Encode workflow item to post as form data; --data-urlencode is not working, using urlencode function as workaround
uriencode () {
  s="${1//'%'/%25}"
  s="${s//' '/%20}"
  s="${s//'"'/%22}"
  s="${s//'#'/%23}"
  s="${s//'$'/%24}"
  s="${s//'&'/%26}"
  s="${s//'+'/%2B}"
  s="${s//','/%2C}"
  s="${s//'/'/%2F}"
  s="${s//':'/%3A}"
  s="${s//';'/%3B}"
  s="${s//'='/%3D}"
  s="${s//'?'/%3F}"
  s="${s//'@'/%40}"
  s="${s//'['/%5B}"
  s="${s//']'/%5D}"
  echo "$s"
  return 0
}


WORKFLOW_ITEM_ENCODED="$(uriencode ${WORKFLOW_ITEM})"


Echo "Retrying workflow"
curl -u ${AEM_LOGIN} -H CSRF-Token:${AEM_TOKEN} ${REFERER}${WF_SERVLET} -H 'Content-Type: application/x-www-form-urlencoded; charset=UTF-8'  --data 'cmd=advance' --data "item=${WORKFLOW_ITEM_ENCODED}" --data "route-${WORKFLOW_ITEM_ENCODED}=retry-current-step" --data "comment-${WORKFLOW_ITEM_ENCODED}=${COMMENT}"
