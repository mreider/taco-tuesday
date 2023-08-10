# Workflow

The JSON definition is [here](workflow-definitions.json).

## Event trigger:

```
event.name == "Order Failure" 
```

## Log query:

```
fetch logs, from:now() - 10m
| sort timestamp desc
| filter contains(content,"unsuccessful")
| filter loglevel == "ERROR"
| fields timestamp, content, trace_id, dt.entity.cloud_application
| filter dt.entity.cloud_application == "CLOUD_APPLICATION-FB9A330C49396123"
```

## Slack message:

```
There are major taco failures.

Traces:

{% for log in result('get_the_logs').records %}
  - <https://bmm59542.dev.apps.dynatracelabs.com/ui/apps/dynatrace.classic.distributed.traces/#trace;traceId={{ log.trace_id }} | {{ log.trace_id }}>
{% endfor %}

<https://bmm59542.dev.apps.dynatracelabs.com/ui/apps/dynatrace.classic.kubernetes.workloads/ui/entity/{{result("get_the_logs").records[0]['dt.entity.cloud_application']}}|Delivery Workload>
```
