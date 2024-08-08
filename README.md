# Hazelcast repeated connection minimal example

## Environment
- Ubuntu 20.04
- microk8s with local docker registry enabled
```
sudo apt update
sudo snap install microk8s --classic

sudo usermod -a -G microk8s $USER
sudo chown -f -R $USER ~/.kube

# Reboot and login again, so we don't have to use `sudo` everytime
sudo reboot

# After reboot:
microk8s status --wait-ready

# Store microk8s config
microk8s config > ~/.kube/config

# Enable DNS
microk8s enable dns

# Enable local registry
microk8s.enable registry
```
- istio 1.20.4
```
# install istioctl 
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.20.4 TARGET_ARCH=x86_64 sh -

# install istio control plane
istioctl install --set profile=demo -y

# Configure istio sidecars
kubectl label namespace hc-minimal istio-injection=enabled

kubectl apply -n hc-minimal -f - <<EOF
apiVersion: security.istio.io/v1beta1
kind: PeerAuthentication
metadata:
  name: default
spec:
  mtls:
    mode: STRICT
EOF
```

## Running

1. Run `kubectl create namespace hc-minimal`
2. Run `kubectl apply -f k8s-config.yaml -n hc-minimal`
3. Run `kubectl get pods -o wide -n hc-minimal` to view the running pod.
4. Once the pod is in a `READY` state, run `kubectl rollout restart deployment hc-minimal -n hc-minimal` to perform a rolling restart.
5. Observe repeated messages in the new pod that started in step 3. These will continue to recur every ~15s indefinitely.

```
2024-08-08 22:38:59 - [10.1.14.167]:5701 [hc-minimal] [5.3.5] Connection[id=68, /10.1.14.167:40679->/10.1.14.178:5701, qualifier=null, endpoint=[10.1.14.178]:5701, remoteUuid=null, alive=false, connectionType=NONE, planeIndex=-1] closed. Reason: Exception in Connection[id=68, /10.1.14.167:40679->/10.1.14.178:5701, qualifier=null, endpoint=[10.1.14.178]:5701, remoteUuid=null, alive=true, connectionType=NONE, planeIndex=-1], thread=hz.hc-minimal.IO.thread-in-3
], thread=hz.hc-minimal.IO.thread-in-3
java.net.SocketException: Connection reset
        at java.base/sun.nio.ch.SocketChannelImpl.throwConnectionReset(SocketChannelImpl.java:401)
        at java.base/sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:434)
        at com.hazelcast.internal.networking.nio.NioInboundPipeline.process(NioInboundPipeline.java:115)
        at com.hazelcast.internal.networking.nio.NioThread.processSelectionKey(NioThread.java:383)
        at com.hazelcast.internal.networking.nio.NioThread.processSelectionKeys(NioThread.java:368)
        at com.hazelcast.internal.networking.nio.NioThread.selectLoop(NioThread.java:294)
        at com.hazelcast.internal.networking.nio.NioThread.executeRun(NioThread.java:249)
        at com.hazelcast.internal.util.executor.HazelcastManagedThread.run(HazelcastManagedThread.java:111)
2024-08-08 22:38:59 - [10.1.14.167]:5701 [hc-minimal] [5.3.5] Removing connection to endpoint [10.1.14.178]:5701 Cause => java.net.SocketException {Connection reset}, Error-Count: 6
2024-08-08 22:39:14 - [10.1.14.167]:5701 [hc-minimal] [5.3.5] Connection[id=69, /10.1.14.167:43755->/10.1.14.178:5701, qualifier=null, endpoint=[10.1.14.178]:5701, remoteUuid=null, alive=false, connectionType=NONE, planeIndex=-1] closed. Reason: Exception in Connection[id=69, /10.1.14.167:43755->/10.1.14.178:5701, qualifier=null, endpoint=[10.1.14.178]:5701, remoteUuid=null, alive=true, connectionType=NONE, planeIndex=-1], thread=hz.hc-minimal.IO.thread-in-0
java.net.SocketException: Connection reset
        at java.base/sun.nio.ch.SocketChannelImpl.throwConnectionReset(SocketChannelImpl.java:401)
        at java.base/sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:434)
        at com.hazelcast.internal.networking.nio.NioInboundPipeline.process(NioInboundPipeline.java:115)
        at com.hazelcast.internal.networking.nio.NioThread.processSelectionKey(NioThread.java:383)
        at com.hazelcast.internal.networking.nio.NioThread.processSelectionKeys(NioThread.java:368)
        at com.hazelcast.internal.networking.nio.NioThread.selectLoop(NioThread.java:294)
        at com.hazelcast.internal.networking.nio.NioThread.executeRun(NioThread.java:249)
        at com.hazelcast.internal.util.executor.HazelcastManagedThread.run(HazelcastManagedThread.java:111)
2024-08-08 22:39:14 - [10.1.14.167]:5701 [hc-minimal] [5.3.5] Removing connection to endpoint [10.1.14.178]:5701 Cause => java.net.SocketException {Connection reset}, Error-Count: 7
2024-08-08 22:39:29 - [10.1.14.167]:5701 [hc-minimal] [5.3.5] Connection[id=70, /10.1.14.167:49515->/10.1.14.178:5701, qualifier=null, endpoint=[10.1.14.178]:5701, remoteUuid=null, alive=false, connectionType=NONE, planeIndex=-1] closed. Reason: Exception in Connection[id=70, /10.1.14.167:49515->/10.1.14.178:5701, qualifier=null, endpoint=[10.1.14.178]:5701, remoteUuid=null, alive=true, connectionType=NONE, planeIndex=-1], thread=hz.hc-minimal.IO.thread-in-1
java.net.SocketException: Connection reset
        at java.base/sun.nio.ch.SocketChannelImpl.throwConnectionReset(SocketChannelImpl.java:401)
        at java.base/sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:434)
        at com.hazelcast.internal.networking.nio.NioInboundPipeline.process(NioInboundPipeline.java:115)
        at com.hazelcast.internal.networking.nio.NioThread.processSelectionKey(NioThread.java:383)
        at com.hazelcast.internal.networking.nio.NioThread.processSelectionKeys(NioThread.java:368)
        at com.hazelcast.internal.networking.nio.NioThread.selectLoop(NioThread.java:294)
        at com.hazelcast.internal.networking.nio.NioThread.executeRun(NioThread.java:249)
        at com.hazelcast.internal.util.executor.HazelcastManagedThread.run(HazelcastManagedThread.java:111)
2024-08-08 22:39:29 - [10.1.14.167]:5701 [hc-minimal] [5.3.5] Removing connection to endpoint [10.1.14.178]:5701 Cause => java.net.SocketException {Connection reset}, Error-Count: 8
...
```

To test any changes to the application, simply run `docker build -t localhost:32000/hc-minimal . && docker push localhost:32000/hc-minimal` before re-deploying.


## Notes

### .take()
The issue stops after commenting out the `take()` call in line 28 in `InfoController.kt` and uncommenting the `poll()` call in line 29:

```
        val testqVal: String? = testq.take() // Using this line causes the error
        // val testqVal: String? = testq.poll() // Using this line works
```
This seems to imply that call to `.take()` causes the issue to occur.

### The issue does NOT appear to be with Istio

When Istio sidecar injection is disabled, we still observe Hazelcast TRACE logs that show connection errors:

```
2024-08-07 21:10:19 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is not yet in progress
2024-08-07 21:10:20 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:20 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:20 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:20 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:34 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is not yet in progress
2024-08-07 21:10:35 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:35 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:35 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:35 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:49 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is not yet in progress
2024-08-07 21:10:50 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:50 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:50 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
2024-08-07 21:10:50 - [10.1.155.20]:5701 [hc-minimal] [5.3.5] Connection to: [10.1.155.51]:5701 streamId:0 is already in progress
```

Enabling Istio simply makes the issue more noticeable. 

