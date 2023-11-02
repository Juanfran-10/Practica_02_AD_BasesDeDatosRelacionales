CREATE TABLE ALUMNOS(
cCodAlu VARCHAR2(6) CONSTRAINT PK_ALUMNOS PRIMARY KEY,
cNomAlu VARCHAR2(100) NOT NULL
);

CREATE TABLE CURSOS(
cCodCurso VARCHAR2(6) CONSTRAINT PK_CURSOS PRIMARY KEY,
cNomCurso VARCHAR2(100) NOT NULL,
nNumExa NUMBER(3) DEFAULT 1 NOT NULL
);

CREATE TABLE MATRICULAS(
cCodAlu VARCHAR2(6) NOT NULL,
cCodCurso VARCHAR2(6) NOT NULL,
nNotaMedia NUMBER(3) DEFAULT 0 NOT NULL,
CONSTRAINT PK_MATRICULAS PRIMARY KEY (cCodAlu,cCodCurso)
);

ALTER TABLE MATRICULAS ADD CONSTRAINT FK_MATRICULAS_ALUMNO FOREIGN KEY (cCodAlu)
REFERENCES ALUMNOS(cCodAlu);

ALTER TABLE MATRICULAS ADD CONSTRAINT FK_MATRICULAS_CURSOS FOREIGN KEY (cCodCurso)
REFERENCES CURSOS(cCodCurso);

CREATE TABLE EXAMENES(
cCodAlu VARCHAR2(6) NOT NULL,
cCodCurso VARCHAR2(6) NOT NULL,
nNumExam NUMBER(3) DEFAULT 1 NOT NULL,
dFecExam DATE,
nNotaExam NUMBER(6,2) DEFAULT 0 NOT NULL,
CONSTRAINT PK_EXAMENES PRIMARY KEY (cCodAlu,cCodCurso,nNumExam)
);

ALTER TABLE EXAMENES ADD CONSTRAINT FK_EXAMENES_MATR FOREIGN KEY (cCodAlu,cCodCurso)
REFERENCES MATRICULAS(cCodAlu,cCodCurso);

INSERT INTO ALUMNOS VALUES ('001','Antonio');
INSERT INTO ALUMNOS VALUES ('002','Maria');
INSERT INTO CURSOS VALUES ('I001','Ingles Basico',5);
INSERT INTO CURSOS VALUES ('I002','Ingles Intermedio',8);
INSERT INTO CURSOS VALUES ('I003','Ingles Avanzado',10);
INSERT INTO CURSOS VALUES ('F001','Frances Basico',3);
INSERT INTO CURSOS VALUES ('C002','Chino Intermedio',9);
COMMIT;
--------------------------------------------------------------------------------

--un procedimiento almacenado en Oracle que generará un registro en la tabla Matriculas 
--para el alumno seleccionado en el Jtable de alumnos y el curso seleccionado en el Jtable
--de Cursos y tantos exámenes como indique el curso.
--Hay que controlar los posibles errores de la ejecución del procedimiento.
CREATE OR REPLACE PROCEDURE sp_AltaMatricula (
  xcCodAlu ALUMNOS.cCodAlu%TYPE,
  xcCodCurso CURSOS.cCodCurso%TYPE,
  xError OUT NUMBER
)
IS
  vNumExa CURSOS.nNumExa%TYPE;
BEGIN
  -- Obtener el número de exámenes permitidos para el curso
  SELECT nNumExa INTO vNumExa FROM CURSOS WHERE cCodCurso = xcCodCurso;

  -- Insertar un registro en la tabla MATRICULAS
  INSERT INTO MATRICULAS (cCodAlu, cCodCurso, nNotaMedia)
  VALUES (xcCodAlu, xcCodCurso, 0);

  -- Insertar registros de exámenes para el alumno y el curso
  FOR i IN 1..vNumExa LOOP
    INSERT INTO EXAMENES (cCodAlu, cCodCurso, nNumExam, dFecExam, nNotaExam)
    VALUES (xcCodAlu, xcCodCurso, i, '', 0);
  END LOOP;

  -- Realizar un COMMIT para confirmar las inserciones
  COMMIT;
  xError := 0; -- Indicar éxito
EXCEPTION
  WHEN NO_DATA_FOUND THEN
    -- Si no se encuentra el curso, establecer xError en -2
    xError := -2;
  WHEN OTHERS THEN
    -- Cualquier otro error, establecer xError en -1
    xError := -1;
END;
--------------------------------------------------------------------------------

--VISTA PARA SACAR LOS DATOS NECESARIOS PARA INSERTALOS EN EL JTABLE_MATRICULAS
CREATE OR REPLACE VIEW V_MATRICULAS AS
    SELECT M.CCODALU, A.CNOMALU, M.CCODCURSO, C.CNOMCURSO, M.NNOTAMEDIA
    FROM MATRICULAS M INNER JOIN ALUMNOS A ON M.CCODALU = A.CCODALU
    INNER JOIN CURSOS C ON M.CCODCURSO = C.CCODCURSO
    ORDER BY M.CCODALU;

--------------------------------------------------------------------------------
CREATE OR REPLACE TRIGGER tr_Examenes_Upd
AFTER UPDATE ON EXAMENES
FOR EACH ROW
DECLARE
    XNNOTAMEDIA MATRICULAS.nNotaMedia%TYPE;
    XNNUMEXA CURSOS.nNumExa%TYPE;
BEGIN
    -- Obtén la nota media anterior y el número de exámenes del curso
    SELECT nNotaMedia INTO XNNOTAMEDIA
    FROM MATRICULAS
    WHERE cCodAlu = :NEW.cCodAlu AND cCodCurso = :NEW.cCodCurso;

    SELECT nNumExa INTO XNNUMEXA
    FROM CURSOS
    WHERE cCodCurso = :NEW.cCodCurso;

    -- Actualiza la nota media en la tabla MATRICULAS
    UPDATE MATRICULAS M
    SET M.nNotaMedia = (XNNOTAMEDIA * XNNUMEXA - :OLD.nNotaExam + :NEW.nNotaExam) / XNNUMEXA
    WHERE M.cCodAlu = :NEW.cCodAlu AND M.cCodCurso = :NEW.cCodCurso;
END;

--------------------------------------------------------------------------------









