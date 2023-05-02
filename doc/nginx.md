## nginx 도입 이유
비용을 최소화하고자 EKS와 ALB 리소스를 사용하지 않고 EC2에 직접 kubernetes 클러스터를 구성하였습니다.  
이에 따라, 외부에서 url-shortener 앱에 접근하기 위한 ingress 사용이 제한되었기에 대체 방법으로 NodePort 리소스를 아래와 같이 생성하였습니다.
```
apiVersion: v1
kind: Service
metadata:
  name: url-shortener
spec:
  ports:
  - name: http
    nodePort: 30790
    port: 8080
    protocol: TCP
    targetPort: 8080
  selector:
    app: url-shortener
  type: NodePort
```
80포트를 30790 포트로 프록시 하기 위해 nginx를 아래와 같이 도입하여 문제를 해결하였습니다.
```
server {
    listen       80;
    listen       [::]:80;
    server_name  43.200.64.114;
    location / {
        proxy_pass http://43.200.64.114:30790;
        proxy_set_header Host $host:$server_port;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header x-forwarded-port $server_port;
    }
```