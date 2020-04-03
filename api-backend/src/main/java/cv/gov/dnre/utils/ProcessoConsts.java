package cv.gov.dnre.utils;

import cv.gov.dnre.Controller.Processo;

public class ProcessoConsts {

    //TIPO IMPOSTO
    private static String tpImpIVA       = "IVA";
    private static String tpImpRetfonte  = "RET_FONT";
    private static String tpImpTEUSimple = "TEU_SIMPLE";
    private static String tpImpTEUColect = "TEU_COLECT";

    //AVISOS ENTREGA DECLARAÇÃO
    private static String codeAvisoEntregaDec            = "AV_ENT";
    private static String codeAvisoEntregaDecPrimeira    = "ENT_PRI";
    private static String codeAvisoEntregaDecSubtituicao = "ENT_SUB";

    //AVISOS ENTREGA IMPOSTO
    private static String codeAvisoEntregaImp         = "AV_ENT_IMP";

    //PROCEDIMENTOS
    private static String codeProcedRedCoima = "PRC";

    public static String getCodeProcedRedCoima() {
        return codeProcedRedCoima;
    }

    //PROCESSOS
    private static String codeProcContra = "PROC_CON";
    private static String codeProcExec   = "PROC_EXE";

    //SUBPROCESSO
    private static String codeSubProcExecFiscal = "PEF";
    private static String codeSubProcExecCoima = "PEC";

    public Processo ProcessoCode(){
        Processo processo = null;
        return null;
    }




}
