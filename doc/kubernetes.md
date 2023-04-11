## EC2 쿠버네티스 싱글 노드 클러스터 Set-up

### 1. 구성환경
* 사양: t3.small (스팟 인스턴스)
  * k8s 요구사항 (2 vCpu / 2 Gib Memory)
* OS: Amazon Linux 2 AMI
* 스토리지: 8 GiB 정적 및 동적 스토리지 운영
* 싱글노드 (마스터 단일노드)

### 2. 클러스터 설치 과정
```
# docker 설치
sudo yum update -y
sudo amazon-linux-extras install docker -y
sudo yum install docker -y
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo docker info

# k8s 레포등록
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF

# k8s 설치
# 보안모듈 비활성화
sudo setenforce 0
sudo sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config

sudo yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
sudo systemctl enable kubelet && sudo systemctl start kubelet

# k8s 클러스터 초기화 및 cidr 설정
sudo kubeadm init --pod-network-cidr=10.244.0.0/16

# 권한설정
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# CNI 설치 (Flannel addon)
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml

# k8s 설치 확인
kubectl get nodes
kubectl get pod -A

# helm 설치
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh
helm version

# 초기화
sudo kubeadm reset -y
sudo yum remove kubelet kubeadm kubectl -y
```

### 3. 동적 스토리지 할당
```
# aws-ebs-csi 설치
sudo yum install -y git
kubectl apply -k "github.com/kubernetes-sigs/aws-ebs-csi-driver/deploy/kubernetes/overlays/stable/?ref=master"

# EC2 역할 부여
- AmazonEC2FullAccess
- AmazonEBSCSIDriverPolicy

# StorageClass 생성
--- storage-class.yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: ebs-gp2
provisioner: ebs.csi.aws.com
parameters:
  type: gp2
reclaimPolicy: Retain
mountOptions:
  - debug
volumeBindingMode: Immediate
---

# PVC 바인딩
--- some-pvc.yaml
apiVersion: v1
kind: PersistentVolumeClaim
...
spec:
  resources:
    requests:
      storage: 8Gi
  storageClassName: ebs-gp2
  ...
---
```