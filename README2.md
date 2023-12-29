# Istio Installation on Azure Kubernetes Service (AKS)

## Overview


This repository provides instructions for installing Istio service mesh on Azure Kubernetes Service (AKS). Istio is installed in the `aks-istio-system` namespace, and the Istio ingress gateway is created in an additional namespace called `aks-istio-ingress`, which is initially empty.

Because Istio is not deployed into the default `istio-system` namespace, all `istioctl` commands should use the `-i aks-istio-system` flag.

## Verify Istio Version

To verify the Istio version installed in the `aks-istio-system` namespace, use the following command:

```bash

istioctl -i aks-istio-system version



## Labeling resources in AKS with Istio

If you want to label the YAML files, you can label the default namespace (`default`) with a specific Istio revision. For example, to label with Istio revision "asm-1-17," use the following command:


```bash
kubectl label namespace default istio.io/rev=asm-1-17

