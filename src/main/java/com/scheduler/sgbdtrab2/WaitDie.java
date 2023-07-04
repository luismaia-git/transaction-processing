package com.scheduler.sgbdtrab2;
public class WaitDie {
    public String execute(Integer ti, Integer tj, TrManager trManager, LockTable lockTable, Graph grafo){
        if(ti < tj ){

            trManager.setStatus(ti, "esperando");
            grafo.insertEdge(ti, tj );
            return "esperando";
        }else{
            trManager.setStatus(ti, "abortada");
            //remover da locktable os bloqueios
            lockTable.commit(ti);
            return "abortada";
        }
    }
}
