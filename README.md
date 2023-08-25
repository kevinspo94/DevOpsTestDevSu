# Aplicación demo-devops-java 

Este repositorio contiene una aplicación de demostración simple escrita en [Java] y una serie de prácticas de DevOps aplicadas a la misma. El objetivo es dockerizar la aplicación, crear un pipeline de integración y despliegue continuo (CI/CD) y desplegarla en un clúster de Kubernetes.

## Aplicación

La aplicación en este repositorio es una aplicación basada en [Java] que y springboot que permite manejar usuarios.


## Pipeline CI/CD Aplicacion

![image](https://github.com/kevinspo94/demo-devops-java/assets/86612020/db80ff98-37e4-4f3d-8b0f-a5d16d63fc5d)



El pipeline CI/CD se establece utilizando [Jenkins]. El pipeline incluye los siguientes pasos:

1. **Github Checkout**: Descarga el código del repositorio para su procesamiento en el entorno de CI/CD.
2. **Construcción del Código**: Compila el código fuente y lo prepara para las pruebas.
3. **Pruebas Unitarias**: Ejecuta pruebas unitarias para garantizar la corrección del código.
4. **Análisis de Código Estático**: Realiza un análisis estático del código para identificar posibles problemas.
5. **Cobertura de Código**: Mide la cobertura de código para evaluar la exhaustividad de las pruebas.
6. **Construcción de Docker** Construye una imagen de Docker.
7. **Publicación de Docker**:  Sube la imagen de Docker a un registro de contenedores.
8. **Despliegue en Kubernetes**: Despliega la aplicación en Kubernetes.
9. **Acciones Posteriores Declarativas**: Refieren a instrucciones que puedes proporcionar en tu flujo de trabajo de CI/CD después de que se completen ciertas etapas o trabajos.


## Pipeline Infraestructura as Code (Terraform)

![image](https://github.com/kevinspo94/demo-devops-java/assets/86612020/5e802037-c81a-45c7-a73c-8ce49b0dd376)

El pipeline CI/CD se establece utilizando [Jenkins]. El pipeline incluye los siguientes pasos:

1. **Github Checkout**: Descarga el código del repositorio para su procesamiento en el entorno de CI/CD.
2. **Setup:** Aquí se incluyen las configuraciones iniciales necesarias antes de ejecutar Terraform, como asegurarse de tener las herramientas y dependencias correctas en tu entorno.
3. **Set Terraform path:** En este paso, debes configurar la variable de entorno TF_PATH para apuntar al directorio donde está instalado Terraform en tu sistema.
4. **TF Plan:** Al ejecutar terraform plan, se analiza la configuración y se genera un plan que muestra los cambios que Terraform planea aplicar sin realizar ninguna acción real.
5. **TF Apply:** El comando terraform apply se utiliza para aplicar los cambios planificados en tu configuración de Terraform a la infraestructura real.
6. **TF Destroy:** Si necesitas eliminar los recursos creados por Terraform, puedes ejecutar terraform destroy para deshacer todos los cambios realizados.




## Despliegue en Kubernetes

La aplicación se despliega en dos clústers de Kubernetes; uno local y otro en la nube, utilizando [Docker Desktop] y [Amazon Elastic Kubernetes Server] respectivamente. 


## Diagramas

[Inserta diagramas relevantes, como el diagrama del pipeline CI/CD, la arquitectura de despliegue en Kubernetes, etc.]

[ Arquitectura de despliegue en Kubernetes con Docker Desktop]

![image](https://github.com/kevinspo94/demo-devops-java/assets/86612020/a48cda3f-8466-4969-a93a-1f7a787c2ba9)



[Arquitectura de despliegue en Kubernetes con EKS]

![image](https://github.com/kevinspo94/demo-devops-java/assets/86612020/91f105da-3132-4cf5-816d-28a8e482feae)



[HPA]

![image](https://github.com/kevinspo94/demo-devops-java/assets/86612020/9c62db60-26e4-420f-b9cc-e1d84a8414b3)


[HPA con Prometheus]


![image](https://github.com/kevinspo94/demo-devops-java/assets/86612020/d32effd9-d79c-4f72-9609-0a43661e6ec5)




## Reaultados de despliegues

La aplicación se desplego localmente y con AWS account free tier por lo que aqui se puede ver los resultados mediante capturas:

[Despliegue en Kubernetes con Docker Desktop]


![Captura de pantalla de 2023-08-25 06-26-34](https://github.com/kevinspo94/demo-devops-java/assets/86612020/3b344cd0-0ed8-4ec9-a6a3-add535ced19c)



[Despliegue en Kubernetes con EKS]

![aws](https://github.com/kevinspo94/demo-devops-java/assets/86612020/b12e43a0-3ae5-4fce-92e7-e73494292457)



## Licencia

Este proyecto está bajo la Licencia [Devsu] - consulta en el prtal web (https://devsu.com/) para más detalles.


