import 'package:enterprise_info/bloc/provider.dart';
import 'package:enterprise_info/controller/company_controller.dart';
import 'package:enterprise_info/model/company.dart';
import 'package:flutter/material.dart';

class CompaniesPage extends StatefulWidget {
  CompaniesPage({Key? key}) : super(key: key);

  @override
  _CompaniesPageState createState() => _CompaniesPageState();
}

class _CompaniesPageState extends State<CompaniesPage> {
  CompanyControler controller = CompanyControler();
  List<int> selected = [];

  @override
  Widget build(BuildContext context) {
    CompanyBloc bloc = Provider.of(context).companyBloc;
    return Scaffold(
      appBar: AppBar(
        actions: <Widget>[
          IconButton(icon: const Icon(Icons.add), onPressed: addCompany),
          IconButton(onPressed: filterCompanies, icon: const Icon(Icons.search))
        ],
      ),
      body: Container(
          child: FutureBuilder(
              future: getCompanies(),
              builder: (BuildContext context,
                  AsyncSnapshot<List<Company>> companies) {
                if (companies.hasData) {
                  return getCards(companies.data!, bloc);
                }
                return Text("Vacio");
              })),
    );
  }

  void filterCompanies() {
    Navigator.pushNamed(context, "filter");
  }

  void addCompany() {
    Navigator.pushNamed(context, 'add');
  }

  Future<List<Company>> getCompanies() async {
    return await controller.getCompanies();
  }

  ListView getCards(List<Object> data, CompanyBloc bloc) {
    List<Company> companies = [];
    for (dynamic val in data) {
      companies.add(val);
    }
    print("ACTUALIZADO \n $companies");
    return ListView.builder(
        itemCount: companies.length,
        itemBuilder: (context, index) {
          return Card(
            child: Row(
              children: [
                Column(children: [
                  Text(companies[index].name),
                  Text(companies[index].url),
                  Text(companies[index].phoneNumber),
                  Text(companies[index].email),
                  Text(companies[index].classification),
                  const SizedBox(
                    height: 10,
                  )
                ]),
                const SizedBox(
                  width: 130,
                ),
                IconButton(
                  alignment: Alignment.centerRight,
                  onPressed: () {
                    updateCompany(bloc, companies[index]);
                  },
                  icon: const Icon(Icons.change_circle, color: Colors.grey),
                ),
                IconButton(
                    padding: EdgeInsets.only(right: 0),
                    alignment: Alignment.centerRight,
                    onPressed: () {
                      setState(() {
                        if (selected.contains(index)) {
                          controller.deleteCompany(companies[index].position!);
                          companies.removeAt(index);
                        } else {
                          selected = [];
                          selected.add(index);
                        }
                      });
                    },
                    icon: Icon(
                      Icons.delete,
                      color:
                          selected.contains(index) ? Colors.red : Colors.grey,
                    ))
              ],
            ),
          );
        });
  }

  void updateCompany(CompanyBloc bloc, Company company) {
    bloc.changeId(company.position!);
    bloc.changeName(company.name);
    bloc.changePhone(company.phoneNumber);
    bloc.changeUrl(company.url);
    bloc.changeEmail(company.email);
    bloc.changeProducts(company.products.toString());
    bloc.changeClassification(company.classification);
    Navigator.pushNamed(context, 'update');
  }
}
