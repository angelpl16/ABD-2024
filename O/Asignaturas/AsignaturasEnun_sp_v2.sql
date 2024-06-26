drop table asignaturas cascade constraints;

create table asignaturas(
  idAsignatura  integer,
  nombre        varchar(20) not null,
  titulacion    varchar(20),
  ncreditos     integer,
  constraint PK_Asignaturas primary key ( idAsignatura, titulacion ),
  constraint UNQ_Asignaturas unique (nombre, titulacion) 
);

create or replace procedure insertaAsignatura(
  v_idAsignatura integer, v_nombreAsig varchar, v_titulacion varchar, v_ncreditos integer) is
  
    same_id exception;
    same_name exception;
    
    PRAGMA EXCEPTION_INIT (same_id, -20000);
    PRAGMA EXCEPTION_INIT (same_name, -20001);
    
    msg_error VARCHAR(255);
    contains_pk INTEGER;

begin
    insert into asignaturas values (v_idAsignatura, v_nombreAsig, v_titulacion, v_ncreditos);
    
    if SQL%ROWCOUNT=1 then
        commit;
    else 
        rollback;
    end if;
    
exception
    when others then
        if SQLCODE=-0001 then           
            msg_error := SQLERRM;
            --La funcion instr devuelve la posicion de comienzo de la subcadena (derecha) en la cadena (izquierda), en caso de no haber coincidencias el valor devuelto es 0.
            --El valor obtenido se almacena en la variable contains_pk.
            select instr(msg_error,'PK_ASIGNATURAS') into contains_pk
            from dual;
            --En caso de que no haya coincidencias con 'PK_ASIGNATURAS' supondremos que el error obtenido es el unique.
            if contains_pk = 0 then
                raise_application_error(-20001,'La asignatura con nombre='||v_nombreAsig||' esta repetida enla titulacion '||v_titulacion||'.');
                
            --En caso de que no entre en el error del unique es que hay coincidencias y el error es el de la PK.    
            else
                raise_application_error(-20000,'La asignatura con idAsignatura='||v_idAsignatura||' esta repetida en la titulacion '||v_titulacion||'.');
            end if;
        end if;    
end;
/

--juego de pruebas automáticas
create or replace procedure test_asignaturas is
  begin
      begin --bloque comun de inicializaciones
        delete from asignaturas;
        insert into asignaturas values ( 1, 'ALGEBRA', 'GRADO INFORMATICA', 6);
        insert into asignaturas values ( 1, 'ALGEBRA', 'GRADO MECANICA', 6);
        commit;
      end;
      
      begin
        insertaAsignatura ( 2, 'ALGEBRA', 'GRADO INFORMATICA', 6);
        dbms_output.put_line('Mal: No detecta error combinacion nombre asignatura + titulación repetida');
      exception
        when others then
          if sqlcode=-20001 then
            dbms_output.put_line('Bien: si detecta error combinacion nombre asignatura + titulación repetida');
            dbms_output.put_line(SQLERRM);
            dbms_output.put_line('');
          else
            dbms_output.put_line('Mal: No detecta error combinacion nombre asignatura + titulación repetida');
            dbms_output.put_line('error='||SQLCODE||'=>'||SQLERRM);
          end if;
      end;
      
      begin
        insertaAsignatura ( 1, 'PROGRAMACION', 'GRADO INFORMATICA', 6);
        dbms_output.put_line('Mal: No detecta error combinacion id asignatura + titulación repetida');
      exception
        when others then
           if sqlcode=-20000 then
            dbms_output.put_line('Bien: si detecta error combinacion id asignatura + titulación repetida');
            dbms_output.put_line(SQLERRM);
            dbms_output.put_line('');
          else
            dbms_output.put_line('Mal: No detecta error combinacion id asignatura + titulación repetida');
            dbms_output.put_line('error='||SQLCODE||'=>'||SQLERRM);
          end if;
      end;
      
      declare
        v_valorEsperado varchar(100):='1ALGEBRAGRADO INFORMATICA6#1ALGEBRAGRADO MECANICA6#2PROGRAMACIONGRADO INFORMATICA6';
        v_valorActual   varchar(100);
      begin
       insertaAsignatura ( 2, 'PROGRAMACION', 'GRADO INFORMATICA', 6);
       rollback; --por si se olvido hacer commit en insertaAsignatura

        SELECT listagg(idAsignatura||nombre||titulacion||ncreditos, '#')
          within group (order by idAsignatura, titulacion) todoJunto
        into v_valorActual
        FROM asignaturas;
      
        
        if v_valorEsperado=v_valorActual then
          dbms_output.put_line('Bien: Caso sin excepciones computado correctamente');
        else
          dbms_output.put_line('Mal: Caso sin excepciones computado incorrectamente');
          dbms_output.put_line('Valor actual=  '||v_valorActual);
          dbms_output.put_line('Valor esperado='||v_valorEsperado);
        end if;
        
   exception
        when others then
          dbms_output.put_line('Mal: Salta excepcion en el caso correcto');
          dbms_output.put_line('error='||SQLCODE||'=>'||SQLERRM);     
    end;
    
  end;
  /

set serveroutput on
exec test_asignaturas;
select * from asignaturas;
commit;
