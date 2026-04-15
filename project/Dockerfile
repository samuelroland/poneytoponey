# This is only for demo purpose ! See README section Demo, for instructions.
# Just a way to easily run "tmate", then "just rmi" and "just client" for non Linux users
FROM fedora:43
RUN dnf install -y java-21-openjdk java-21-openjdk-devel tmate just iproute
RUN dnf install -y clear
WORKDIR /project
ENTRYPOINT [ "tail", "-f", "/dev/null" ]
