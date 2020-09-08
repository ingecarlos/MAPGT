package com.example.george.enrutamiento.ODKData;

import android.os.Environment;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class ODKData {
    /*formulario a graficar*/
    public static File formulario;
    /*para debug*/
    private static final String ODKDATA = "ODKData";
    /*constantes*/
    private static final String FORM_NAME = "uso de tiempo"; /*no se necesita el nombre completo solo una seccion */
    private static final int CANTIDAD_OBSERVACIONES = 10;

    //material segun estructura en el formulario odk
    private static final int NINGUN_MATERIAL = 2;
    private static final int LIBROS = 3;
    private static final int CUADERNOS = 4;
    private static final int PIZARRA = 5;
    private static final int TIC = 6;
    private static final int DIDACTICO = 7;
    private static final int COOPERATIVA = 8;
    /*NOTA: para el choices "material" cambian los valores*/
    private static final int M_NINGUN_MATERIAL = 1;
    private static final int M_LIBROS = 2;
    private static final int M_CUADERNOS = 3;
    private static final int M_PIZARRA = 4;
    private static final int M_DIDACTICO = 5;
    private static final int M_TIC = 6;
    private static final int M_COOPERATIVA = 7;


    //materiales cadena a desplegar
    public static String NINGUN_MATERIAL_STR = "Ningún material";
    public static String LIBROS_STR = "Libros";
    public static String CUADERNOS_STR = "Cuadernos";
    public static String PIZARRA_STR = "Pizarra";
    public static String TIC_STR = "TIC";
    public static String DIDACTICO_STR = "Didácticos";
    public static String COOPERATIVA_STR = "Cooperativo";


    public static String CUADERNOS_STR_LARGE = "Cuadernos e instrumentos para escribir";
    public static String DIDACTICO_STR_LARGE = "Recursos didácticos";
    public static String COOPERATIVA_STR_LARGE = "Material para trabajo en grupo/equipo";

    //DEFINICION ENGAGE
    private static final int UNO = 1;
    private static final int PEQUENO = 2;
    private static final int GRANDE = 3;
    private static final int TODOS = 4;

    private static final int UNO_VAL = 5;
    private static final int PEQUENO_VAL = 25;
    private static final int GRANDE_VAL = 75;
    private static final int TODOS_VAL = 100;

    /*ACTIVIDADES*/
    public static final int LEYENDO = 1;
    public static final int DEMOSTRANDO = 2;
    public static final int PREGUNTAS = 3;
    public static final int PRACTICANDO = 4;
    public static final int TAREA = 5;
    public static final int COPIANDO = 6;
    public static final int INSTRUCCIONVERBAL = 7;
    public static final int DISCIPLINANDO = 8;
    public static final int ORGANIZACION = 9;
    public static final int GESTION = 10;
    public static final int SOCIALALUMNOS = 11;
    public static final int SOCIALEXTERNAS = 12;
    public static final int FUERAAULA = 13;

    public static String LEYENDO_STR = "Leyendo en voz alta";
    public static String DEMOSTRANDO_STR = "Enseñando";
    public static String PREGUNTAS_STR = "Debate";
    public static String PRACTICANDO_STR = "Memorizando";
    public static String TAREA_STR = "Tarea/Ejercicios";
    public static String COPIANDO_STR = "Copiando";
    public static String INSTRUCCIONVERBAL_STR = "Instrucción verbal";
    public static String DISCIPLINANDO_STR = "Disciplinando";
    public static String ORGANIZACION_STR = "Organizando aula";
    public static String GESTION_STR = "Gestión";
    public static String SOCIALALUMNOS_STR = "IS profesor y alumnos";
    public static String SOCUALEXTERNAS_STR = "IS externa";
    public static String FUERAAULA_STR = "Fuera del aula";

    public static String DEMOSTRANDO_STR_LARGE = "Demostrando/Enseñando";
    public static String PREGUNTAS_STR_LARGE = "Preguntas y respuestas/Debate";
    public static String PRACTICANDO_STR_LARGE = "Practicando/memorizando";
    public static String ORGANIZACION_STR_LARGE = "Organizando el aula con los estudiantes";
    public static String GESTION_STR_LARGE = "Gestión del aula solo";
    public static String SOCIALALUMNOS_STR_LARGE = "Interacción social entre profesor y alumnos";
    public static String SOCUALEXTERNAS_STR_LARGE = "Interacción social con personas externas/Profesor no involucrado";
    public static String FUERAAULA_STR_LARGE = "Profesor fuera del aula";

    /*QUE DATOS TRAER*/
    private static final int MATERIALES = 1;
    private static final int MATERIALESINVOLUCRAMIENTO = 2;
    private static final int ACTIVIDADES = 3;
    private static final int PROMEDIOINVOLUCRAMIENTO = 4;
    private static final int ACTIVIDADESINVOLUCRAMIENTO = 5;


    /*variables*/
    private static boolean isFileManagerInitialized = false;
    private static Document doc;
    private static String XMLROOT;

    /*dataset Salida*/
    public static Map<String, Integer> dataSetMateriales;
    public static Map<Integer, float[]> dataSetMaterialesInvolucramiento;
    public static Map<String, Integer> dataSetActividades;
    public static Map<Integer, String> labels5;
    public static Map<Integer, String> labels3;
    public static Map<Integer, Integer> dataSetPromedioInvolucramiento;
    public static Map<Integer, float[]> dataSetActividadesInvolucramiento;


    /*CLASIFICACION DE ACTIVIDADES*/
    public static final int ACTIVIDADAPRENDIZAJE = 1;
    public static final int ACTIVIDADORGANIZACION = 2;
    public static final int ACTIVIDADNOINVOLUCRADO = 3;

    //static File formulario;//formulario a graficar


    public static void getDataSetMateriales() {
        getData(MATERIALES);
    }

    public static void getDataSetMaterialesInvolucramiento() {
        getData(MATERIALESINVOLUCRAMIENTO);
    }

    public static void getDataSetActividadesInvolucramiento() {
        getData(ACTIVIDADESINVOLUCRAMIENTO);
    }

    public static void getDataSetActividades() {
        getData(ACTIVIDADES);
    }

    public static void getDataSetPromedioInvolucramiento() {
        getData(PROMEDIOINVOLUCRAMIENTO);
    }


    public static void getData(final int data) {
        try {
            switch (data) {
                case MATERIALES:
                    parseMateriales(formulario);
                    break;
                case MATERIALESINVOLUCRAMIENTO:
                    parseMaterialesInvolucramiento(formulario);
                    break;
                case ACTIVIDADES:
                    parseActividades(formulario);
                    break;
                case PROMEDIOINVOLUCRAMIENTO:
                    parsePromedioInvolucramiento(formulario);
                    break;
                case ACTIVIDADESINVOLUCRAMIENTO:
                    parseActividadesInvolucramiento(formulario);
                    break;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }


    }

    public static List<Map<String, Object>> getFormulariosRecientes() {
        List<File> formularios = getFormularios();
        List<Map<String, Object>> data = new ArrayList<>();


        for (File form : formularios) {

            Map<String, Object> metaData = parseMetaData(form);
            if (metaData != null) data.add(metaData);
        }


        return data;
    }

    private static List<File> getFormularios() {
        //if (!isFileManagerInitialized) {
        final String rootPath = String.valueOf(Environment.getExternalStorageDirectory()) + "/odk/instances";
        final File dir = new File(rootPath);
        List<File> formularios = new ArrayList<>();
        explorarDir(dir, formularios);

        return formularios;
    }

    private static void explorarDir(File root, List<File> datos) {
        if (root.isDirectory()) {
            File[] contenido = root.listFiles();
            if (root.getAbsolutePath().contains(FORM_NAME)) {
                datos.add(root);
            }

            for (int j = 0; j < contenido.length; j++) {
                explorarDir(contenido[j], datos);
            }

        }
    }

    private static void parseMateriales(File formulario) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        dataSetMateriales = new HashMap<String, Integer>();

        String xmlContent = getXmlFromForm(formulario);
        XPath xPath = getXpath(xmlContent);

        //obteniendo el root del xml
        String expression = "/";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                doc, XPathConstants.NODESET);
        XMLROOT = nodeList.item(0).getFirstChild().getNodeName();


        List<Integer> ningun = new ArrayList<>();
        extraerMaterial(ningun, NINGUN_MATERIAL, xPath);
        List<Integer> libros = new ArrayList<>();
        extraerMaterial(libros, LIBROS, xPath);
        List<Integer> cuadernos = new ArrayList<>();
        extraerMaterial(cuadernos, CUADERNOS, xPath);
        List<Integer> pizarra = new ArrayList<>();
        extraerMaterial(pizarra, PIZARRA, xPath);
        List<Integer> tic = new ArrayList<>();
        extraerMaterial(tic, TIC, xPath);
        List<Integer> didactico = new ArrayList<>();
        extraerMaterial(didactico, DIDACTICO, xPath);
        List<Integer> cooperativa = new ArrayList<>();
        extraerMaterial(cooperativa, COOPERATIVA, xPath);

        //debug("----------" + NINGUN_MATERIAL_STR + "----------");
        for (int e : ningun) {
            //debug("" + e);
        }
        //debug("----------" + LIBROS_STR + "----------");
        for (int e : libros) {
            //debug("" + e);
        }
        //debug("----------" + CUADERNOS_STR + "----------");
        for (int e : cuadernos) {
            //debug("" + e);
        }
        //debug("----------" + PIZARRA_STR + "----------");
        for (int e : pizarra) {
            //debug("" + e);
        }
        //debug("----------" + TIC_STR + "----------");
        for (int e : tic) {
            //debug("" + e);
        }
        //debug("----------" + DIDACTICO_STR + "----------");
        for (int e : didactico) {
            //debug("" + e);
        }
        //debug("----------" + COOPERATIVA_STR + "----------");
        for (int e : cooperativa) {
            //debug("" + e);
        }


        if (ningun.size() > 0) {
            dataSetMateriales.put(NINGUN_MATERIAL_STR, getSumatoria(ningun));
        }
        if (libros.size() > 0) {
            dataSetMateriales.put(LIBROS_STR, getSumatoria(libros));
        }
        if (cuadernos.size() > 0) {
            dataSetMateriales.put(CUADERNOS_STR_LARGE, getSumatoria(cuadernos));
        }
        if (pizarra.size() > 0) {
            dataSetMateriales.put(PIZARRA_STR, getSumatoria(pizarra));
        }
        if (tic.size() > 0) {
            dataSetMateriales.put(TIC_STR, getSumatoria(tic));
        }
        if (didactico.size() > 0) {
            dataSetMateriales.put(DIDACTICO_STR_LARGE, getSumatoria(didactico));
        }
        if (cooperativa.size() > 0) {
            dataSetMateriales.put(COOPERATIVA_STR_LARGE, getSumatoria(cooperativa));
        }


    }

    private static void parseMaterialesInvolucramiento(File formulario) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        dataSetMaterialesInvolucramiento = new HashMap<Integer, float[]>();

        String xmlContent = getXmlFromForm(formulario);
        XPath xPath = getXpath(xmlContent);

        //obteniendo el root del xml
        String expression = "/";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                doc, XPathConstants.NODESET);
        XMLROOT = nodeList.item(0).getFirstChild().getNodeName();


        List<Integer> ningun = new ArrayList<>();
        extraerMaterialInvolucramiento(ningun, NINGUN_MATERIAL, xPath);
        List<Integer> libros = new ArrayList<>();
        extraerMaterialInvolucramiento(libros, LIBROS, xPath);
        List<Integer> cuadernos = new ArrayList<>();
        extraerMaterialInvolucramiento(cuadernos, CUADERNOS, xPath);
        List<Integer> pizarra = new ArrayList<>();
        extraerMaterialInvolucramiento(pizarra, PIZARRA, xPath);
        List<Integer> tic = new ArrayList<>();
        extraerMaterialInvolucramiento(tic, TIC, xPath);
        List<Integer> didactico = new ArrayList<>();
        extraerMaterialInvolucramiento(didactico, DIDACTICO, xPath);
        List<Integer> cooperativa = new ArrayList<>();
        extraerMaterialInvolucramiento(cooperativa, COOPERATIVA, xPath);

        Log.d("CRUELA", "----NINGUN----");
        Log.d("CRUELA", java.util.Arrays.toString(ningun.toArray()));

        Log.d("CRUELA", "----LIBROS----");
        Log.d("CRUELA", java.util.Arrays.toString(libros.toArray()));

        Log.d("CRUELA", "---CUADERNOS----");
        Log.d("CRUELA", java.util.Arrays.toString(cuadernos.toArray()));

        Log.d("CRUELA", "----PIZZARRA---");
        Log.d("CRUELA", java.util.Arrays.toString(pizarra.toArray()));

        Log.d("CRUELA", "----TIC----");
        Log.d("CRUELA", java.util.Arrays.toString(tic.toArray()));

        Log.d("CRUELA", "----didactico----");
        Log.d("CRUELA", java.util.Arrays.toString(didactico.toArray()));

        Log.d("CRUELA", "----cooperativa----");
        Log.d("CRUELA", java.util.Arrays.toString(cooperativa.toArray()));


        labels5 = new HashMap<>();

        int label = 100;
        int asteriscoContador = 1;

        if (ningun.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(ningun));
            labels5.put(label, NINGUN_MATERIAL_STR);
            label--;
        }
        if (libros.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(libros));
            labels5.put(label, LIBROS_STR);
            label--;
        }
        if (cuadernos.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(cuadernos));
            CUADERNOS_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels5.put(label, CUADERNOS_STR);
            label--;
        } else {
            CUADERNOS_STR_LARGE += "$NO$";
        }
        if (pizarra.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(pizarra));
            labels5.put(label, PIZARRA_STR);
            label--;
        }
        if (tic.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(tic));
            labels5.put(label, TIC_STR);
            label--;
        }
        if (didactico.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(didactico));
            DIDACTICO_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels5.put(label, DIDACTICO_STR);
            label--;
        } else {
            DIDACTICO_STR_LARGE += "$NO$";
        }
        if (cooperativa.size() > 0) {
            dataSetMaterialesInvolucramiento.put(label, getSumatoriaVsTotal(cooperativa));
            COOPERATIVA_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels5.put(label, COOPERATIVA_STR);
            label--;
        } else {
            COOPERATIVA_STR_LARGE += "$NO$";
        }


    }

    private static void parseActividadesInvolucramiento(File formulario) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        dataSetActividadesInvolucramiento = new HashMap<>();

        String xmlContent = getXmlFromForm(formulario);
        XPath xPath = getXpath(xmlContent);

        //obteniendo el root del xml
        String expression = "/";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                doc, XPathConstants.NODESET);
        XMLROOT = nodeList.item(0).getFirstChild().getNodeName();


        List<Integer> leyendo = new ArrayList();
        List<Integer> demostrando = new ArrayList();
        List<Integer> preguntas = new ArrayList();
        List<Integer> practicando = new ArrayList();
        List<Integer> tarea = new ArrayList();
        List<Integer> copiando = new ArrayList();
        List<Integer> instruccionVerbal = new ArrayList();
        List<Integer> disciplinando = new ArrayList();
        List<Integer> organizacion = new ArrayList();
        List<Integer> gestion = new ArrayList();
        List<Integer> socialAlumnos = new ArrayList();
        List<Integer> socialExtermans = new ArrayList();
        List<Integer> FueraAula = new ArrayList();


        for (int observacion = 1; observacion <= CANTIDAD_OBSERVACIONES; observacion++) {

            int involucrados = 0;
            int actividad = -1;
            /*extraer que hace el profesor*/
            int actividadProfesor = -1;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                actividadProfesor = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                actividad = actividadProfesor;
            }

            /*actividades de aprendizaje con el profesor*/
            if (clasificar(actividadProfesor) == ACTIVIDADAPRENDIZAJE) {
                expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i2";
                nodeList = (NodeList) xPath.compile(expression).evaluate(
                        doc, XPathConstants.NODESET);
                if (nodeList.getLength() > 0) {
                    int engage = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                    int involucradosConDocente = engageToPercent(engage);
                    involucrados = involucradosConDocente;
                }
            }

            int involucradosSinDocente = 0; /*grupo grande,mediano,pequeño o uno*/
            /*actividades de aprendizaje sin el profesor*/
            int aprendizaje = 0;
            int noAprendizaje = 0;

            /*esta validación se hace porque en caso de que se cumplan las condiciones de abajo
             * se debe suponer que todos los estudiantes estan involucrados
             * */
            /* h3 = 10,12 o 13 */
            List<String> h3 = null;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
            //debug(expression);
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                h3 = Arrays.asList(nodeList.item(0).getFirstChild().getNodeValue().split(" "));
            }

            /* i5 < 2 */
            int i5 = 10;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i5";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                i5 = nodeList.item(0).getFirstChild().getNodeValue().split(" ").length;
            }

            for (char grupo = 'J'; grupo <= 'O'; grupo++) {
                for (int material = 2; material <= 8; material++) {
                    /* GRUPO1 contains (MATERIAL) y GRUPO1 < 2 */

                    int grupo1;
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + "1";
                    //+ material;
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);

                    if (nodeList.getLength() > 0) {
                        String valor = nodeList.item(0).getFirstChild().getNodeValue();
                        List<String> valores = Arrays.asList(valor.split(" "));
                        grupo1 = valores.size();

                        if (h3 != null && (h3.contains("10") || h3.contains("12") || h3.contains("13"))
                                && i5 < 2
                                && grupo1 < 2 && mismoMaterial(Integer.parseInt(valor), material)) {
                            if (clasificar(Integer.parseInt(valor)) == ACTIVIDADAPRENDIZAJE) {
                                aprendizaje += engageToInt(TODOS);
                            } else {
                                noAprendizaje += engageToInt(TODOS);
                            }
                        }

                    }

                    /*
                     * Esta es la validación normal
                     */
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + material;
                    //debug(expression);
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);

                    if (nodeList.getLength() > 0) {
                        int valor = engageToInt(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));
                        aprendizaje += valor;
                        //debug(nodeList.item(0).getFirstChild().getNodeValue());
                    }
                }
            }

            /*las que no son de aprendizaje i6, i7 e i8*/
            expression = "/" + XMLROOT + "/G[" + observacion + "]/i6";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                int valor = engageToInt(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));
                noAprendizaje += valor;
                //debug(nodeList.item(0).getFirstChild().getNodeValue());
            }


            if (involucrados != 0) {
                involucradosSinDocente = getInvolucradosPercent(aprendizaje, noAprendizaje);
                double percentToAdd = ((double) (100 - involucrados) / (double) 100) * involucradosSinDocente;
                involucrados += percentToAdd;
            } else {
                involucrados = getInvolucradosPercent(aprendizaje, noAprendizaje);
            }

            int involucramiento = involucrados;


            switch (actividad) {
                case LEYENDO:
                    leyendo.add(involucramiento);
                    break;
                case DEMOSTRANDO:
                    demostrando.add(involucramiento);
                    break;
                case PREGUNTAS:
                    preguntas.add(involucramiento);
                    break;
                case PRACTICANDO:
                    practicando.add(involucramiento);
                    break;
                case TAREA:
                    tarea.add(involucramiento);
                    break;
                case COPIANDO:
                    copiando.add(involucramiento);
                    break;
                case INSTRUCCIONVERBAL:
                    instruccionVerbal.add(involucramiento);
                    break;
                case DISCIPLINANDO:
                    disciplinando.add(involucramiento);
                    break;
                case ORGANIZACION:
                    organizacion.add(involucramiento);
                    break;
                case GESTION:
                    gestion.add(involucramiento);
                    break;
                case SOCIALALUMNOS:
                    socialAlumnos.add(involucramiento);
                    break;
                case SOCIALEXTERNAS:
                    socialExtermans.add(involucramiento);
                    break;
                case FUERAAULA:
                    FueraAula.add(involucramiento);
                    break;
            }
        }


        int label = 100;
        int asteriscoContador = 1;
        labels3 = new HashMap<>();

        if (leyendo.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(leyendo));
            labels3.put(label, LEYENDO_STR);
            label--;
        }
        if (demostrando.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(demostrando));
            DEMOSTRANDO_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, DEMOSTRANDO_STR);
            label--;
        } else {
            DEMOSTRANDO_STR_LARGE += "$NO$";
        }
        if (preguntas.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(preguntas));
            PREGUNTAS_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, PREGUNTAS_STR);
            label--;
        } else {
            PREGUNTAS_STR_LARGE += "$NO$";
        }
        if (practicando.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(practicando));
            PRACTICANDO_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, PRACTICANDO_STR);
            label--;
        } else {
            PRACTICANDO_STR_LARGE += "$NO$";
        }
        if (tarea.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(tarea));
            labels3.put(label, TAREA_STR);
            label--;
        }
        if (copiando.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(copiando));
            labels3.put(label, COPIANDO_STR);
            label--;
        }
        if (instruccionVerbal.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(instruccionVerbal));
            labels3.put(label, INSTRUCCIONVERBAL_STR);
            label--;
        }
        if (disciplinando.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(disciplinando));
            labels3.put(label, DISCIPLINANDO_STR);
            label--;
        }
        if (organizacion.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(organizacion));
            ORGANIZACION_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, ORGANIZACION_STR);
            label--;
        } else {
            ORGANIZACION_STR_LARGE += "$NO$";
        }
        if (gestion.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(gestion));
            GESTION_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, GESTION_STR);
            label--;
        } else {
            GESTION_STR_LARGE += "$NO$";
        }
        if (socialAlumnos.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(socialAlumnos));
            SOCIALALUMNOS_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, SOCIALALUMNOS_STR);
            label--;
        } else {
            SOCIALALUMNOS_STR_LARGE += "$NO$";
        }
        if (socialExtermans.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(socialExtermans));
            SOCUALEXTERNAS_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, SOCUALEXTERNAS_STR);
            label--;
        } else {
            SOCUALEXTERNAS_STR_LARGE += "$NO$";
        }
        if (FueraAula.size() > 0) {
            dataSetActividadesInvolucramiento.put(label, getSumatoriaVsTotal(FueraAula));
            FUERAAULA_STR += " *" + asteriscoContador;
            asteriscoContador++;

            labels3.put(label, FUERAAULA_STR);
            label--;
        } else {
            FUERAAULA_STR_LARGE += "$NO$";
        }


    }


    private static void parseActividades(File formulario) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        dataSetActividades = new HashMap<String, Integer>();

        String xmlContent = getXmlFromForm(formulario);
        XPath xPath = getXpath(xmlContent);

        //obteniendo el root del xml
        String expression = "/";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                doc, XPathConstants.NODESET);
        XMLROOT = nodeList.item(0).getFirstChild().getNodeName();

        List<Integer> actividades = new ArrayList<>();

        /*extraer materiales*/
        for (int observacion = 1; observacion <= CANTIDAD_OBSERVACIONES; observacion++) {
            expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                int actividad = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                actividades.add(actividad);
                //debug("actividad: " + actividad);
            }
        }

        int aprendizaje = 0;
        int organizacion = 0;
        int noInvolucrado = 0;

        for (int actividad : actividades) {
            int clase = clasificar(actividad);
            if (clase == ACTIVIDADAPRENDIZAJE)
                aprendizaje += 1;
            else if (clase == ACTIVIDADORGANIZACION)
                organizacion += 1;
            else if (clase == ACTIVIDADNOINVOLUCRADO)
                noInvolucrado += 1;
        }


        if (aprendizaje > 0)
            dataSetActividades.put("Aprendizaje", aprendizaje);
        if (organizacion > 0)
            dataSetActividades.put("Organización del Aula", organizacion);
        if (noInvolucrado > 0)
            dataSetActividades.put("Docente no involucrado", noInvolucrado);


    }

    private static Map<String, Object> parseMetaData(File formulario) {
        try {

            String mensaje = "";

            Map<String, Object> metaData = new HashMap<String, Object>();
            metaData.put("archivo", formulario);

            String xmlContent = getXmlFromForm(formulario);
            XPath xPath = getXpath(xmlContent);

            String expression = "/";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            XMLROOT = nodeList.item(0).getFirstChild().getNodeName();


            //extraer codigo
            expression = "/" + XMLROOT + "/grupo_1/codigo";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                String codigo = nodeList.item(0).getFirstChild().getNodeValue();
                metaData.put("codigo", codigo);
                mensaje += codigo;
            }

            //extraer datos centro
            expression = "/" + XMLROOT + "/grupo_1/datos";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                String datos = nodeList.item(0).getFirstChild().getNodeValue();
                String regex = "<[^>]*>";
                String output = datos.replaceAll(regex, "");
                output = output.replaceAll("\n", " ");
                output = output.replaceAll("Nombre del CE: ", ",");
                output = output.replaceAll("Dirección del CE: ", ", Dirección: ");
                output = output.replaceAll("Modalidad del CE:.*", "");

                metaData.put("datos", output);
                mensaje += output;
            }
            //exraer fecha
            expression = "/" + XMLROOT + "/fechaObservacion";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                String fecha = nodeList.item(0).getFirstChild().getNodeValue();
                metaData.put("fecha", fecha);
            }
            //exraer grado
            expression = "/" + XMLROOT + "/B/b8";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            String grado = "";
            if (nodeList.getLength() > 0) {
                String grado_ = nodeList.item(0).getFirstChild().getNodeValue();
                if (grado_.equals("1")) {
                    grado = "1ro Básico";
                    //metaData.put("grado", "1ro Básico");
                } else if (grado_.equals("2")) {
                    grado = "2do Básico";
                    //metaData.put("grado", "2do Básico");
                } else if (grado_.equals("3")) {
                    grado = "3ro Básico";
                    //metaData.put("grado", "3ro Básico");
                }
            }

            //exraer seccion
            expression = "/" + XMLROOT + "/B/b9";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                String seccion = nodeList.item(0).getFirstChild().getNodeValue();
                metaData.put("grado", grado + " Sección " + seccion);
            }
            //exraer asignatura
            expression = "/" + XMLROOT + "/B/b12";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                String asignatura_ = nodeList.item(0).getFirstChild().getNodeValue();
                if (asignatura_.equals("1")) {
                    metaData.put("asignatura", "Comunicación y lenguaje");
                } else if (asignatura_.equals("2")) {
                    metaData.put("asignatura", "Ciencias naturales");
                } else if (asignatura_.equals("3")) {
                    metaData.put("asignatura", "Matemáticas");
                } else if (asignatura_.equals("4")) {
                    metaData.put("asignatura", "Culturas e Idiomas Mayas");
                } else if (asignatura_.equals("5")) {
                    metaData.put("asignatura", "Ciencias Sociales y ciudadanía");
                }
            }


            metaData.put("centro", mensaje);

            return metaData;

        } catch (Exception e) {
            return null;
        }


    }

    private static void parsePromedioInvolucramiento(File formulario) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        dataSetPromedioInvolucramiento = new HashMap<Integer, Integer>();

        String xmlContent = getXmlFromForm(formulario);
        XPath xPath = getXpath(xmlContent);

        //obteniendo el root del xml
        String expression = "/";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                doc, XPathConstants.NODESET);
        XMLROOT = nodeList.item(0).getFirstChild().getNodeName();

        for (int observacion = 1; observacion <= CANTIDAD_OBSERVACIONES; observacion++) {
            int involucrados = 0;

            /*extraer que hace el profesor*/
            int actividadProfesor = -1;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                actividadProfesor = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
            }

            /*actividades de aprendizaje con el profesor*/
            if (clasificar(actividadProfesor) == ACTIVIDADAPRENDIZAJE) {
                expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i2";
                nodeList = (NodeList) xPath.compile(expression).evaluate(
                        doc, XPathConstants.NODESET);
                if (nodeList.getLength() > 0) {
                    int engage = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                    int involucradosConDocente = engageToPercent(engage);
                    involucrados = involucradosConDocente;
                }
            }

            int involucradosSinDocente = 0; /*grupo grande,mediano,pequeño o uno*/
            /*actividades de aprendizaje sin el profesor*/
            int aprendizaje = 0;
            int noAprendizaje = 0;

            /*esta validación se hace porque en caso de que se cumplan las condiciones de abajo
             * se debe suponer que todos los estudiantes estan involucrados
             * */
            /* h3 = 10,12 o 13 */
            List<String> h3 = null;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
            //debug(expression);
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                h3 = Arrays.asList(nodeList.item(0).getFirstChild().getNodeValue().split(" "));
            }

            /* i5 < 2 */
            int i5 = 10;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i5";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                i5 = nodeList.item(0).getFirstChild().getNodeValue().split(" ").length;
            }

            for (char grupo = 'J'; grupo <= 'O'; grupo++) {
                for (int material = 2; material <= 8; material++) {
                    /* GRUPO1 contains (MATERIAL) y GRUPO1 < 2 */

                    int grupo1;
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + "1";
                    //+ material;
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);

                    if (nodeList.getLength() > 0) {
                        String valor = nodeList.item(0).getFirstChild().getNodeValue();
                        List<String> valores = Arrays.asList(valor.split(" "));
                        grupo1 = valores.size();

                        if (h3 != null && (h3.contains("10") || h3.contains("12") || h3.contains("13"))
                                && i5 < 2
                                && grupo1 < 2 && mismoMaterial(Integer.parseInt(valor), material)) {
                            if (clasificar(Integer.parseInt(valor)) == ACTIVIDADAPRENDIZAJE) {
                                aprendizaje += engageToInt(TODOS);
                            } else {
                                noAprendizaje += engageToInt(TODOS);
                            }
                        }

                    }

                    /*
                     * Esta es la validación normal
                     */
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + material;
                    //debug(expression);
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);

                    if (nodeList.getLength() > 0) {
                        int valor = engageToInt(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));
                        aprendizaje += valor;
                        //debug(nodeList.item(0).getFirstChild().getNodeValue());
                    }
                }
            }

            /*las que no son de aprendizaje i6, i7 e i8*/
            expression = "/" + XMLROOT + "/G[" + observacion + "]/i6";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                int valor = engageToInt(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));
                noAprendizaje += valor;
                //debug(nodeList.item(0).getFirstChild().getNodeValue());
            }


            if (involucrados != 0) {
                involucradosSinDocente = getInvolucradosPercent(aprendizaje, noAprendizaje);
                double percentToAdd = ((double) (100 - involucrados) / (double) 100) * involucradosSinDocente;
                involucrados += percentToAdd;
            } else {
                involucrados = getInvolucradosPercent(aprendizaje, noAprendizaje);
            }

            dataSetPromedioInvolucramiento.put(observacion, involucrados);

        }


    }


    private static int getSumatoria(List<Integer> lista) {
        int suma = 0;
        for (int valor : lista) {
            suma += valor;
        }
        return suma;
    }

    private static float[] getSumatoriaVsTotal(List<Integer> lista) {
        float suma = 0;
        float pasadas = 0;
        for (int valor : lista) {
            suma += valor;
            pasadas += 1;
        }
        float total = pasadas * 100;
        //debug("suma: " + suma + ", pasadas: " + pasadas + ", total: " + total);
        float p = (float) suma / total;
        float q = (float) ((total - suma) / total);
        //debug("p: " + p + ", q: " + q);

        return new float[]{p * 100, q * 100};
    }


    private static void extraerMaterial(List<Integer> materialCount, int material, XPath xPath) throws XPathExpressionException {
        String expression;
        NodeList nodeList;
        /*extraer materiales*/
        for (int observacion = 1; observacion <= CANTIDAD_OBSERVACIONES; observacion++) {
            /*LOS ALUMNOS QUE DE PRIMERA INSTANCIA PARTICIPAN EN LA ACTIVIDAD*/
            /*que material utiliza*/
            expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i1";
            //debug(expression);
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                int material_ = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                boolean mismoMaterial = mismoMaterial(material_, material);
                //debug(mismoMaterial + "");
                if (mismoMaterial) {
                    /*cuantos lo utilizan*/
                    String expression_ = "/" + XMLROOT + "/G[" + observacion + "]/I/i2";
                    debug(expression_);
                    NodeList nodeList_ = (NodeList) xPath.compile(expression_).evaluate(
                            doc, XPathConstants.NODESET);
                    if (nodeList_.getLength() > 0) {
                        int valor = engageToPercent(Integer.parseInt(nodeList_.item(0).getFirstChild().getNodeValue()));
                        if (valor == -1) {
                            //debug("AQUÍ");
                        }
                        materialCount.add(valor);
                    }
                }
            }


            /*LOS ALUMNOS QUE NO PARTICIPAN. QUE ESTAN HACIENDO?*/


            /*esta validación se hace porque en caso de que se cumplan las condiciones de abajo
             * se debe suponer que todos los estudiantes estan en el complemento
             * */

            /* h3 = 10,12 o 13 */
            List<String> h3 = null;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
            //debug(expression);
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                h3 = Arrays.asList(nodeList.item(0).getFirstChild().getNodeValue().split(" "));
            }


            /* i5 < 2 */
            int i5 = 10;
            expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i5";
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            if (nodeList.getLength() > 0) {
                i5 = nodeList.item(0).getFirstChild().getNodeValue().split(" ").length;
            }

            for (char grupo = 'J'; grupo <= 'O'; grupo++) {
                /* GRUPO1 contains (MATERIAL) y GRUPO1 < 2 */
                int grupo1;
                expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + "1";
                //+ material;
                nodeList = (NodeList) xPath.compile(expression).evaluate(
                        doc, XPathConstants.NODESET);

                if (nodeList.getLength() > 0) {
                    String valor = nodeList.item(0).getFirstChild().getNodeValue();
                    grupo1 = valor.split(" ").length;

                    if (h3 != null && (h3.contains("10") || h3.contains("12") || h3.contains("13"))
                            && i5 < 2
                            && grupo1 < 2 && mismoMaterial(Integer.parseInt(valor), material)) {
                        debug("ACÁ DEBE HACER LA SUPOSICIÓN. observacion: " + observacion + " grupo: " + grupo + "material: " + valor);
                        int valorSupuesto = engageToPercent(TODOS);
                        materialCount.add(valorSupuesto);
                    }

                }



                /*
                 * Esta es la validación normal
                 */
                expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + material;
                //debug(expression);
                nodeList = (NodeList) xPath.compile(expression).evaluate(
                        doc, XPathConstants.NODESET);

                if (nodeList.getLength() > 0) {
                    int valor = engageToPercent(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));

                    materialCount.add(valor);
                    //debug(nodeList.item(0).getFirstChild().getNodeValue());
                }
            }
        }
    }

    private static void extraerMaterialInvolucramiento(List<Integer> materialCount, int material, XPath xPath) throws XPathExpressionException {
        String expression;
        NodeList nodeList;
        /*extraer materiales*/
        for (int observacion = 1; observacion <= CANTIDAD_OBSERVACIONES; observacion++) {
            /*LOS ALUMNOS QUE DE PRIMERA INSTANCIA PARTICIPAN EN LA ACTIVIDAD*/
            /*que material utiliza*/
            expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i1";
            //debug(expression);
            nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);
            if (nodeList.getLength() > 0) {
                int material_ = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                boolean mismoMaterial = mismoMaterial(material_, material);
                //debug(mismoMaterial + "");
                if (mismoMaterial) {


                    int involucrados = 0;
                    int actividad = -1;
                    /*extraer que hace el profesor*/
                    int actividadProfesor = -1;
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);
                    if (nodeList.getLength() > 0) {
                        actividadProfesor = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                        actividad = actividadProfesor;
                    }

                    /*actividades de aprendizaje con el profesor*/
                    if (clasificar(actividadProfesor) == ACTIVIDADAPRENDIZAJE) {
                        expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i2";
                        nodeList = (NodeList) xPath.compile(expression).evaluate(
                                doc, XPathConstants.NODESET);
                        if (nodeList.getLength() > 0) {
                            int engage = Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue());
                            int involucradosConDocente = engageToPercent(engage);
                            involucrados = involucradosConDocente;
                        }
                    }

                    int involucradosSinDocente = 0; /*grupo grande,mediano,pequeño o uno*/
                    /*actividades de aprendizaje sin el profesor*/
                    int aprendizaje = 0;
                    int noAprendizaje = 0;

                    /*esta validación se hace porque en caso de que se cumplan las condiciones de abajo
                     * se debe suponer que todos los estudiantes estan involucrados
                     * */
                    /* h3 = 10,12 o 13 */
                    List<String> h3 = null;
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/H/h3";
                    //debug(expression);
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);

                    if (nodeList.getLength() > 0) {
                        h3 = Arrays.asList(nodeList.item(0).getFirstChild().getNodeValue().split(" "));
                    }

                    /* i5 < 2 */
                    int i5 = 10;
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/I/i5";
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);

                    if (nodeList.getLength() > 0) {
                        i5 = nodeList.item(0).getFirstChild().getNodeValue().split(" ").length;
                    }

                    for (char grupo = 'J'; grupo <= 'O'; grupo++) {
                        /* GRUPO1 contains (MATERIAL) y GRUPO1 < 2 */

                        int grupo1;
                        expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + "1";
                        //+ material;
                        nodeList = (NodeList) xPath.compile(expression).evaluate(
                                doc, XPathConstants.NODESET);

                        if (nodeList.getLength() > 0) {
                            String valor = nodeList.item(0).getFirstChild().getNodeValue();
                            List<String> valores = Arrays.asList(valor.split(" "));
                            grupo1 = valores.size();

                            if (h3 != null && (h3.contains("10") || h3.contains("12") || h3.contains("13"))
                                    && i5 < 2
                                    && grupo1 < 2 && mismoMaterial(Integer.parseInt(valor), material)) {
                                if (clasificar(Integer.parseInt(valor)) == ACTIVIDADAPRENDIZAJE) {
                                    aprendizaje += engageToInt(TODOS);
                                } else {
                                    noAprendizaje += engageToInt(TODOS);
                                }
                            }

                        }

                        /*
                         * Esta es la validación normal
                         */
                        expression = "/" + XMLROOT + "/G[" + observacion + "]/" + grupo + "/" + Character.toLowerCase(grupo) + material;
                        //debug(expression);
                        nodeList = (NodeList) xPath.compile(expression).evaluate(
                                doc, XPathConstants.NODESET);

                        if (nodeList.getLength() > 0) {
                            int valor = engageToInt(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));
                            aprendizaje += valor;
                            //debug(nodeList.item(0).getFirstChild().getNodeValue());
                        }
                    }

                    /*las que no son de aprendizaje i6, i7 e i8*/
                    expression = "/" + XMLROOT + "/G[" + observacion + "]/i6";
                    nodeList = (NodeList) xPath.compile(expression).evaluate(
                            doc, XPathConstants.NODESET);
                    if (nodeList.getLength() > 0) {
                        int valor = engageToInt(Integer.parseInt(nodeList.item(0).getFirstChild().getNodeValue()));
                        noAprendizaje += valor;
                        //debug(nodeList.item(0).getFirstChild().getNodeValue());
                    }


                    if (involucrados != 0) {
                        involucradosSinDocente = getInvolucradosPercent(aprendizaje, noAprendizaje);
                        double percentToAdd = ((double) (100 - involucrados) / (double) 100) * involucradosSinDocente;
                        involucrados += percentToAdd;
                    } else {
                        involucrados = getInvolucradosPercent(aprendizaje, noAprendizaje);
                    }

                    int involucramiento = involucrados;

                    materialCount.add(involucramiento);
                }
            }
        }
    }

    private static int engageToPercent(int engage) {
        switch (engage) {
            case UNO:
                return UNO_VAL;
            case PEQUENO:
                return PEQUENO_VAL;
            case GRANDE:
                return GRANDE_VAL;
            case TODOS:
                return TODOS_VAL;
        }
        return -1;
    }

    private static int engageToInt(int engage) {
        switch (engage) {
            case UNO:
                return 1;
            case PEQUENO:
                return 3;
            case GRANDE:
                return 6;
            case TODOS:
                return 100;
        }
        return -1;
    }


    private static XPath getXpath(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(xmlString);

        ByteArrayInputStream input = new ByteArrayInputStream(
                xmlStringBuilder.toString().getBytes("UTF-8"));

        doc = builder.parse(input);
        XPath xPath = XPathFactory.newInstance().newXPath();
        return xPath;
    }

    private static String getXmlFromForm(File formulario) throws IOException {
        File fl = formulario.listFiles()[0];
        FileInputStream is = new FileInputStream(fl);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        is.close();
        return sb.toString();
    }


    /**/
    private static boolean mismoMaterial(int fromOdk, int mat) {
        //debug(fromOdk + " == " + mat);
        switch (fromOdk) {
            case M_NINGUN_MATERIAL:
                return mat == NINGUN_MATERIAL;
            case M_LIBROS:
                return mat == LIBROS;
            case M_CUADERNOS:
                return mat == CUADERNOS;
            case M_PIZARRA:
                return mat == PIZARRA;
            case M_DIDACTICO:
                return mat == DIDACTICO;
            case M_TIC:
                return mat == TIC;
            case M_COOPERATIVA:
                return mat == COOPERATIVA;
        }
        return false;
    }

    private static void debug(String msg) {
        Log.d(ODKDATA, msg);
    }


    public static Map<Integer, String> getLabels(int grafico) {
        if(grafico==3)return labels3;
        if(grafico==5)return labels5;
        return null;
        /*Map<Integer, String> labels = new HashMap<>();
        labels.put(NINGUN_MATERIAL, NINGUN_MATERIAL_STR);
        labels.put(LIBROS, LIBROS_STR);
        labels.put(CUADERNOS, CUADERNOS_STR);
        labels.put(PIZARRA, PIZARRA_STR);
        labels.put(TIC, TIC_STR);
        labels.put(DIDACTICO, DIDACTICO_STR);
        labels.put(COOPERATIVA, COOPERATIVA_STR);
        */
    }

    private static int clasificar(int actividad) {
        if (actividad == LEYENDO || actividad == DEMOSTRANDO || actividad == PREGUNTAS || actividad == PRACTICANDO || actividad == TAREA || actividad == COPIANDO)
            return ACTIVIDADAPRENDIZAJE;
        else if (actividad == INSTRUCCIONVERBAL || actividad == DISCIPLINANDO || actividad == ORGANIZACION || actividad == GESTION)
            return ACTIVIDADORGANIZACION;
        return ACTIVIDADNOINVOLUCRADO;
    }

    private static int getInvolucradosPercent(int aprendizaje, int noAprendizje) {
        if (aprendizaje == 0) return 0;

        if (aprendizaje >= 6) {
            if (noAprendizje >= 6) {
                return 50;
            } else if (noAprendizje >= 3) {
                return 75;
            } else if (noAprendizje < 3) {
                return 95;
            }
        }
        if (aprendizaje >= 3) {
            if (noAprendizje >= 6) {
                return 25;
            } else if (noAprendizje >= 3) {
                return 50;
            } else if (noAprendizje < 3) {
                return 75;
            }
        } else {
            if (noAprendizje >= 6) {
                return 5;
            } else if (noAprendizje >= 3) {
                return 25;
            } else if (noAprendizje < 3) {
                return 50;
            }
        }
        return -1;
    }

    public static void resetLabels() {

        NINGUN_MATERIAL_STR = "Ningún material";
        LIBROS_STR = "Libros";
        CUADERNOS_STR = "Cuadernos";
        PIZARRA_STR = "Pizarra";
        TIC_STR = "TIC";
        DIDACTICO_STR = "Didácticos";
        COOPERATIVA_STR = "Cooperativa";

        CUADERNOS_STR_LARGE = "Cuadernos e instrumentos para escribir";
        DIDACTICO_STR_LARGE = "Recursos didácticos";
        COOPERATIVA_STR_LARGE = "Actividad y material cooperativa";


        LEYENDO_STR = "Leyendo en voz alta";
        DEMOSTRANDO_STR = "Enseñando";
        PREGUNTAS_STR = "Debate";
        PRACTICANDO_STR = "Memorizando";
        TAREA_STR = "Tarea/Ejercicios";
        COPIANDO_STR = "Copiando";
        INSTRUCCIONVERBAL_STR = "Instrucción verbal";
        DISCIPLINANDO_STR = "Disciplinando";
        ORGANIZACION_STR = "Organizando aula";
        GESTION_STR = "Gestión";
        SOCIALALUMNOS_STR = "IS profesor y alumnos";
        SOCUALEXTERNAS_STR = "IS externa";
        FUERAAULA_STR = "Fuera del aula";


        DEMOSTRANDO_STR_LARGE = "Demostrando/Enseñando";
        PREGUNTAS_STR_LARGE = "Preguntas y respuestas/Debate";
        PRACTICANDO_STR_LARGE = "Practicando/memorizando";
        ORGANIZACION_STR_LARGE = "Organizando el aula con los estudiantes";
        GESTION_STR_LARGE = "Gestión del aula solo";
        SOCIALALUMNOS_STR_LARGE = "Interacción social entre profesor y alumnos";
        SOCUALEXTERNAS_STR_LARGE = "Interacción social con personas externas/Profesor no involucrado";
        FUERAAULA_STR_LARGE = "Profesor fuera del aula";
    }
}
