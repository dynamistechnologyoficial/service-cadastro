package com.banco.cadastro.br;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.banco.cadastro.br.audit.EntityAuditEventListener;
import com.banco.cadastro.br.domain.AbstractAuditingEntity;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packagesOf = ServiceCadastroApp.class, importOptions = DoNotIncludeTests.class)
class TechnicalStructureTest {

    // prettier-ignore
    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Config").definedBy("..config..")
        .layer("Client").definedBy("..client..")
        .layer("Web").definedBy("..web..")
        .optionalLayer("Service").definedBy("..service..")
        .layer("Security").definedBy("..security..")
        .optionalLayer("Persistence").definedBy("..repository..")
        .layer("Domain").definedBy("..domain..")

        .whereLayer("Config").mayNotBeAccessedByAnyLayer()
        .whereLayer("Client").mayNotBeAccessedByAnyLayer()
        .whereLayer("Web").mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Service").mayOnlyBeAccessedByLayers("Web", "Config")
        .whereLayer("Security").mayOnlyBeAccessedByLayers("Config", "Client", "Service", "Web")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service", "Security", "Web", "Config")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Persistence", "Service", "Security", "Web", "Config")

        .ignoreDependency(resideInAPackage("com.banco.cadastro.br.audit"), alwaysTrue())
        .ignoreDependency(type(AbstractAuditingEntity.class), type(EntityAuditEventListener.class))
        .ignoreDependency(belongToAnyOf(ServiceCadastroApp.class), alwaysTrue())
        .ignoreDependency(alwaysTrue(), belongToAnyOf(
            com.banco.cadastro.br.config.Constants.class,
            com.banco.cadastro.br.config.ApplicationProperties.class
        ));
}
