1) Start Docker
sudo systemctl start docker 

2) Create image from Dockerfile
sudo docker build . -t vm:derby_db

3) Start detached container from the image on port 1333:
sudo docker run --name carapp_db -t -d -p 1333:1333 vm:derby_db


