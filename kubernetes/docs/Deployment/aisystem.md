
#### Object: aisystem
#### Type: Deployment

### Cluster에 aisystem Deployment를 생성하려면 아래의 명령어를 실행하세요.

```
$ kubectl create -f - <<EOF 
apiVersion: "apps/v1"
kind: "Deployment"
metadata: 
  name: "aisystem"
  labels: 
    app: "aisystem"
spec: 
  selector: 
    matchLabels: 
      app: "aisystem"
  replicas: 1
  template: 
    metadata: 
      labels: 
        app: "aisystem"
    spec: 
      containers: 
        - 
          name: "aisystem"
          image: "ghcr.io/undefined"
          ports: 
            - 
              containerPort: 8080
          readinessProbe: 
            httpGet: 
              path: "/actuator/health"
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10
          livenessProbe: 
            httpGet: 
              path: "/actuator/health"
              port: 8080
            initialDelaySeconds: 120
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 5
EOF
```
- Yaml 파일에 명시된 스펙으로 aisystem Deployment를 생성합니다.

```
$ kubectl apply -f - <<EOF 
apiVersion: "apps/v1"
kind: "Deployment"
metadata: 
  name: "aisystem"
  labels: 
    app: "aisystem"
spec: 
  selector: 
    matchLabels: 
      app: "aisystem"
  replicas: 1
  template: 
    metadata: 
      labels: 
        app: "aisystem"
    spec: 
      containers: 
        - 
          name: "aisystem"
          image: "ghcr.io/undefined"
          ports: 
            - 
              containerPort: 8080
          readinessProbe: 
            httpGet: 
              path: "/actuator/health"
              port: 8080
            initialDelaySeconds: 10
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 10
          livenessProbe: 
            httpGet: 
              path: "/actuator/health"
              port: 8080
            initialDelaySeconds: 120
            timeoutSeconds: 2
            periodSeconds: 5
            failureThreshold: 5
EOF
```
- Create가 된 상태라면 aisystem Deployment의 수정이 이루어지고, Create가 된 상태가 아니라면 aisystem Deployment를 Create 해주는 명령어입니다.  
#

### aisystem Deployment가 정상적으로 생성되었는지 확인하시려면 아래의 명령어를 실행하세요.

```
$ kubectl get Deployment

NAME            READY   UP-TO-DATE   AVAILABLE   AGE
aisystem           3/3     3            3           5m43s

```
- aisystem Deployment와 Pod, Replicaset이 모두 확인이 된다면 정상적으로 생성된 것입니다.
#

### 생성된 aisystem Deployment의 상세 실행 정보를 확인하시려면 아래의 명령어를 입력하세요.

```
$ kubectl describe Deployment aisystem
```
- aisystem Deployment의 상태를 확인하여 문제가 있는지 확인합니다. 
#

### Kubernetes Cluster network 외부에서 service에 access할 때, 해당 명령어로 외부 IP traffic을 허용할 수 있습니다.

```
$ kubectl port-forward Deployment/aisystem 8080:8080
```
#

### aisystem Deployment를 삭제하려면 아래의 명령어를 실행하세요.

```
$ kubectl delete Deployment aisystem
```
#

