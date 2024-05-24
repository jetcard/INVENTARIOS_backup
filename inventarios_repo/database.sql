CREATE TABLE proveedor (
    id SERIAL PRIMARY KEY,
    nombreproveedor VARCHAR(255) NOT NULL,
    descripproveedor VARCHAR(255) NOT NULL
);

CREATE TABLE grupo (
    id SERIAL PRIMARY KEY,
    nombregrupo VARCHAR(255) NOT NULL,
    descripgrupo VARCHAR(255) NOT NULL
);

CREATE TABLE articulo (
    id SERIAL PRIMARY KEY,
    nombrearticulo VARCHAR(255) NOT NULL,
    descriparticulo VARCHAR(255) NOT NULL
);

CREATE TABLE responsable (
    id SERIAL PRIMARY KEY,
    arearesponsable VARCHAR(255) NOT NULL,
    nombresyapellidos VARCHAR(255) NOT NULL,
    correo VARCHAR(100) NULL,
    nombreusuario VARCHAR(255) NULL
);

CREATE TABLE tipo (
    id SERIAL PRIMARY KEY,
    nombretipo VARCHAR(255) NOT NULL,
    descriptipo VARCHAR(255) NOT NULL
);

CREATE TABLE dependencia (
    id SERIAL PRIMARY KEY,
    nombredependencia VARCHAR(255) NOT NULL,
    descripdependencia VARCHAR(255) NOT NULL
);

CREATE TABLE comun (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    descripcomun VARCHAR(255) NOT NULL,
    descripcortacomun VARCHAR(255) NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (grupoId) REFERENCES grupo(id)
);

CREATE TABLE atributo (
    id SERIAL PRIMARY KEY,
    responsableId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id)
);

CREATE TABLE atributos (
    id SERIAL PRIMARY KEY,
    atributoid INTEGER NULL,
    nombreatributo VARCHAR(255) NOT NULL,
    FOREIGN KEY (atributoid) REFERENCES atributo(id)
);

CREATE TABLE activo (
    id SERIAL PRIMARY KEY,
    codinventario VARCHAR(50)  NULL,
    modelo VARCHAR(255)  NULL,
    marca VARCHAR(255) NOT NULL,
    nroserie VARCHAR(255) NOT NULL,
    fechaingreso DATE NOT NULL,
    importe NUMERIC NOT NULL,
    moneda VARCHAR(10) NOT NULL,
    grupoId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    responsableId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    FOREIGN KEY (grupoId) REFERENCES grupo(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id)
);





CREATE TABLE atributos (
    id SERIAL PRIMARY KEY,
    atributoid INTEGER NOT NULL,
    nombreatributo VARCHAR(255) NOT NULL,
    descripatributo VARCHAR(255) NOT NULL,
    FOREIGN KEY (atributoId) REFERENCES atributo(id)
);

CREATE TABLE activo (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(255)  NULL,
    marca VARCHAR(255) NOT NULL,
    nroserie VARCHAR(255) NOT NULL,
    fechacompra VARCHAR(255) NOT NULL,
    importe INTEGER NOT NULL,
    account INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    responsableId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    FOREIGN KEY (grupoId) REFERENCES grupo(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (responsableId) REFERENCES responsable(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id)
);




CREATE TABLE activo (
    id SERIAL PRIMARY KEY,
    modelo VARCHAR(255)  NULL,
    marca VARCHAR(255) NOT NULL,
    nroserie VARCHAR(255) NOT NULL,
    fechacompra VARCHAR(255) NOT NULL,
    importe INTEGER NOT NULL,
    account INTEGER NOT NULL,
    grupoId INTEGER NOT NULL,
    picture BYTEA NULL,
    FOREIGN KEY (grupoId) REFERENCES grupo(id)
);


 "{\"activoResponse\":{\"listaactivos\":[{\"id\":1,\"modelo\":\"AS\",\"marca\":\"AS\",\"nroserie\":\"1\",\"fechaingreso\":\"1\",\"importe\":777,\"account\":55555,\"grupo\":{\"id\":1,\"nombregrupo\":\"categoria\",\"descripgrupo\":\"saas\"}}]},\"metadata\":[{\"date\":\"Activo guardado\",\"code\":\"00\",\"type\":\"Respuesta ok\"}]}"

sam local start-api --debug

aws apigateway get-domain-names

aws apigateway delete-domain-name --domain-name <your-domain>

MAIN_CLASS	com.inventarios.SpringBootSampleApplication

sam local start-api
aws s3 cp swagger.yaml s3://my-deploy-bucket
localhost:8010

[
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "GET",
            "PUT",
            "POST",
            "DELETE"
        ],
        "AllowedOrigins": [
            "http://d1bpq8ufqb6flq.cloudfront.net",
            "https://d1bpq8ufqb6flq.cloudfront.net"
        ],
        "ExposeHeaders": [],
        "MaxAgeSeconds": 3000
    }
]



aws cloudformation delete-stack --stack-name sam-app
sam delete-stack --stack-name NOMBRE_DEL_STACK

delimiter @

create database if not exists basededatos
@
drop table if exists basededatos.activo
@
create table basededatos.activo (
    id INT NOT NULL AUTO_INCREMENT,
    name varchar(255) CHARACTER SET utf8 NOT NULL,
    email varchar(255) CHARACTER SET utf8 NOT NULL,
    PRIMARY KEY (id)
)


CREATE TABLE IF NOT EXISTS public.activo
(
    id integer NOT NULL,
    name character(255) COLLATE pg_catalog."default",
    email character(255) COLLATE pg_catalog."default",
    CONSTRAINT activo_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.activo
    OWNER to postgres;


      SecretsPolicy:
        Type: 'AWS::IAM::ManagedPolicy'
        Properties:
          Description: Allow Lambdas to access the RDS Managed Secret
          PolicyDocument:
            Version: "2012-10-17"
            Statement:
              - Effect: Allow
                Action:
                  - 'secretsmanager:GetSecretValue'
                  - 'secretsmanager:DescribeSecret'
                Resource: !Ref RDSSecret

      CreateTableFunction:
        Type: AWS::Lambda::Function
        Properties:
          Handler: index.handler
          Role: !GetAtt CreateTableFunctionRole.Arn
          Code:
            ZipFile: |
              const AWS = require('aws-sdk');
              const rdsDataService = new AWS.RDSDataService();

              exports.handler = async (event, context) => {
                const params = {
                  secretArn: '${RDSSecret}',
                  resourceArn: '${RDSInstance}',
                  sql: 'CREATE TABLE activo (id SERIAL PRIMARY KEY, modelo VARCHAR(255) NOT NULL, marca VARCHAR(255) NOT NULL, nroSerie VARCHAR(255) NOT NULL, fechaCompra VARCHAR(255) NOT NULL, importe INTEGER NOT NULL, account INTEGER NOT NULL);',
                  database: 'basededatos'
                };

                try {
                  const data = await rdsDataService.executeStatement(params).promise();
                  console.log(data);
                  return { statusCode: 200, body: JSON.stringify(data) };
                } catch (error) {
                  console.error(error);
                  return { statusCode: 500, body: error.message };
                }
              };
          Runtime: nodejs20.x

      CreateTableFunctionRole:
        Type: AWS::IAM::Role
        Properties:
          AssumeRolePolicyDocument:
            Version: "2012-10-17"
            Statement:
              - Effect: Allow
                Principal:
                  Service: lambda.amazonaws.com
                Action: sts:AssumeRole
          Policies:
            - PolicyName: CreateTableFunctionPolicy
              PolicyDocument:
                Version: "2012-10-17"
                Statement:
                  - Effect: Allow
                    Action:
                      - rds-data:ExecuteStatement
                    Resource: "*"

      CreateTablePermission:
        Type: AWS::Lambda::Permission
        Properties:
          Action: lambda:InvokeFunction
          FunctionName: !GetAtt CreateTableFunction.Arn
          Principal: lambda.amazonaws.com
          SourceArn: !Sub "arn:aws:apigateway:${AWS::Region}:lambda:path/2015-03-31/functions/${CreateTableFunction.Arn}/invocations"

    Outputs:
      SecretArn:
        Description: The Managed Secret ARN
        Value: !Ref RDSSecret
      RDSInstanceEndpoint:
        Description: "Endpoint completo de la instancia de RDS PostgreSQL"
        Value: !GetAtt RDSInstance.Endpoint.Address
      ApiEndpoint:
        Description: "API Gateway endpoint URL for Prod stage for Quarkus sample function"
        Value: !Sub "https://${MyApi}.execute-api.${AWS::Region}.amazonaws.com/prod"
