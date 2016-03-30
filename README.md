Docker CAS Test Image
============================

Docker image used to launch a sample CAS instance for test purpose (for example when developing an application that will require a CAS server).

# Requirements
* [Docker](https://www.docker.com/)

# Build

```bash
git clone https://github.com/iorga-group/docker-cas-test.git
cd docker-cas-test
docker build -t cas-test ./
```

# Launch

```bash
docker run -it --rm -p "8080:8080" -p "8443:8443" --hostname sso.mycompany.com cas-test
```

Associate `sso.mycompany.com` with your localhost, and CAS will be available at:

* `http://sso.mycompany.com:8080/cas`
* `https://sso.mycompany.com:8443/cas`

## Parameters

You can use `CAS_CONFIG` docker environment variable to customize the users / services available in that CAS instance.

This variable must be a JSON string with this format:

```
{
  users: [
    {
      userName: string,
      password: string,
      acceptAnyPassword: boolean (default to false),
      attributes: object (which will be sent as CAS attributes)
    }
  ],
  services: [
    {
      id: number (optional),
      url: string (pattern - java style - used to match a service name),
      allowedUserNames: array of string (containing userNames allowed to access this service),
      attributesOverridesByUserName: object (which key corresponds to the userName for whom you want to override attributes, and value is an object: the attributes to override and their values),
      logoutUrl: string (url called after CAS logout, optional),
      acceptAnyUserName: boolean (default to true if allowedUserNames is empty, false otherwise)
    }
  ],
  acceptAnyLoginAndPassword: boolean (default to true)
  acceptAnyService: boolean (default to true)
}
```

Here is an example:

```bash
docker run -it --rm -p "8080:8080" -p "8443:8443" --hostname sso.mycompany.com -e "CAS_CONFIG={users:[{userName: 'test', acceptAnyPassword: true, attributes: {test: 'ok'}}]}" cas-test
```

## Optimization

You can launch the instances faster using sharing the `/root/.m2` directory with the host.

Example:

```bash
docker run -it --rm -p "8080:8080" -p "8443:8443" -v /home/myuser/.m2:/root/.m2 --hostname sso.mycompany.com cas-test
```